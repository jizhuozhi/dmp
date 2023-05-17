# dmp
Data Mapping Processor

---

## Data Type

- Number (BigInteger / BigDecimal): `1234`, `1234.5678`
- Boolean: `true` / `false`
- String: `'123\'456'`, `"123\"456"`
- Object: `{foo: 123, bar: 456}`

DMP **has no plan** to support array literals, because it is uncontrollable, users can easily create arrays with different types, which is not friendly to client deserialization.

## Expression

All data type literals, and

- origin(`.`): loads origin object which given in `Context`
- projection: loads field by given path and optional maps to an object or array
  1. origin field path(`.foo.bar`): loads `foo` from origin and loads `bar` from `foo`
  2. symbol field path(`foobar.foo.bar`): loads `foo` from symbol `foobar` and loads `bar` from `foo`
  3. optional field path(`foobar?.foo?.bar`): as `foobar.foo.bar` but loads `null` if symbol `foobar` or field `foo` is not exists
  4. object projection(`.foobar.foo(foo -> foo.bar)`): as `.foobar.foo`, but binds value to symbol `foo`, then executes projection `{bar: foo.bar}`. In this example, it's as same as `.foobar.foo.bar`, but `foo.bar` could be any valid expression.
  5. array projection(`.foobar.foo[foo -> foo.bar]`): as `.foobar.foo`, but binds each items in array to symbol `foo`, loads `bar` from each item `foo`, and then pushes to new array.

DMP **has no plan** to support any other operators and function call, DMP **is just a** data mapping processor, but neither a GPPL (general purpose program language) , nor expression language, nor script language.
