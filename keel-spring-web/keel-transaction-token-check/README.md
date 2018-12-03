# 2重送信防止機能
Webブラウザなどで更新系ボタンを複数回押された場合に初回のリクエスト以外をエラーとし、重複登録や複数回の更新を防止する機能を提供する。

本機能で実現できることの詳細は、[macchinetta](https://macchinetta.github.io/server-guideline-thymeleaf/1.5.1.RELEASE/ja/ArchitectureInDetail/WebApplicationDetail/DoubleSubmitProtection.html)を参照。  
※本機能は、`macchinetta`のライブラリを取り込み`Spring Boot`向けに変更を行っている。

## 使い方
### 依存ライブラリの追加
`keel-spring-boot-starter-web`を依存ライブラリに追加する。
`Maven`の場合は、`pom`に`dependencies`に以下を追加する。(バージョンは修正してください。)
```xml
<dependency>
  <groupId>jp.fintan.keel</groupId>
  <artifactId>keel-spring-boot-starter-web</artifactId>
  <version>xxxx</version>
</dependency>
```

依存ライブラリに`keel-spring-boot-starter-web`を追加することで、本ライブラリを使用するための設定が自動的に行われる。
`keel-spring-boot-starter-web`を依存ライブラリに追加するが、2重送信防止機能を使用したくない場合(他の機能だけを使用したい場合)には、`properties(application.properties)`に以下を追加する。  
```properties
keel.transaction.token.enabled=false
```

### 2重送信防止対象のリクエストパスを設定する
`properties(applicatin.properties)`に2重送信防止対象のリクエストパス及び除外リクエストパスを設定する。

**設定例**  
```properties
# すべてのリクエストパスが2重送信防止対象
keel.transaction.token.path-patterns=/**
# /resources配下のリクエストについては、2重送信防止対象から除外する
keel.transaction.token.exclude-path-patterns=/resources/**
```
複数のパスを設定したい場合には、以下のようにカンマ(,)区切りで複数パスを設定する
```properties
keel.transaction.token.path-patterns=/user/**,/book/**
keel.transaction.token.exclude-path-patterns=/resources/**,/**/*.html
```

### 2重送信防止をアプリケーションに実装する
登録や更新処理の流れが、`入力(編集)`→`確認`→`完了`の場合で、登録や更新処理を行う`確認`→`完了`で2重送信防止処理を行う場合を例に実装方法を説明する。

※サンプルコード全体は、[UserController](../../keel-spring-enhance-samples/keel-transaction-token-check-sample/src/main/java/jp/fintan/keel/spring/sample/web/token/UserController.java) を参照

1. Controllerクラスに2重送信防止処理で使用するトークンを管理するためのネームスペースを設定する  
  `Controller`クラスに`jp.fintan.keel.spring.web.token.transaction.TransactionTokenCheck`アノテーションを設定し、ネームスペースを指定する。  
  ```java
  @Controller
  // TransactionTokenCheckのvalue属性にネームスペースを設定する。
  // 設定する値は、Controllerのクラス名(Controllerを削除したもの)などを設定するとわかりやすい
  @TransactionTokenCheck("user")
  public class UserController {
    // 省略
  }
  ```
2. 2重送信防止処理を開始するリクエストを明示的に指定する  
  このサンプルでは、`確認`→`完了`時に2重送信防止処理を行うため、`確認`のリクエストが2重送信防止処理の開始リクエストとなる。  
  `確認`のリクエストに、`jp.fintan.keel.spring.web.token.transaction.TransactionTokenCheck`アノテーションを設定し、`type`属性に
  2重送信防止処理の開始を示す`jp.fintan.keel.spring.web.token.transaction.TransactionTokenType.BEGIN`を設定する。  
  ```java
  // 2重送信防止処理の開始リクエストであることを示すアノテーションを設定する
  @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
  @PostMapping("/confirm")
  public String confirm(@Validated UserForm form, BindingResult bindingResult) {
    // 省略
  }
  ```

3. 2重送信防止処理を行うリクエストを指定する  
  `確認`→`完了`の処理を行うリクエストに、2重送信防止のチェックを行うことを示す`jp.fintan.keel.spring.web.token.transaction.TransactionTokenCheck`アノテーションを設定する。  
  デフォルト状態でチェック処理が行われるため、アノテーションの属性指定は不要となる。
  ```java
  // 2重送信防止チェックを行うことを示すアノテーションを設定する。
  @TransactionTokenCheck
  @Transactional
  @PostMapping("/create")
  public String create(@Validated UserForm form, BindingResult bindingResult) {
    // 省略
  }
  ```

4. 2重送信防止チェックでエラーとなった場合のハンドリング処理を実装する  
  2重送信防止チェクでエラーとなった場合は、`jp.fintan.keel.spring.web.token.transaction.InvalidTransactionTokenException`が送出されるため、
  例外ハンドリングでこの例外を補足しレスポンスの返却を行えば良い。  
  このサンプルでは、単一の機能のみとなっているためControllerクラスにて例外ハンドリングを行っている。  
  Controllerに実装してしまうと全てのControllerで同じような例外ハンドリングを実装する必要が出てくるため、
  `ControllerAdvice`(`org.springframework.web.bind.annotation.ControllerAdvice`アノテーションを設定したクラス)にて例外ハンドリングを行うと良い。
  ```java
  @ExceptionHandler(InvalidTransactionTokenException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public String invalidTransactionTokenExceptionHandler(InvalidTransactionTokenException e) {
    // 省略
  }
  ```
