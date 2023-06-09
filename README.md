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

Because DMP doesn't implement symbol scoping, the symbol for each mapping binding should be unique within the current scope (`it -> ...`), otherwise it will be overwritten.

DMP **has no plan** to support any other operators and function call, DMP **is just a** data mapping processor, but neither a GPPL (general purpose program language) , nor expression language, nor script language.

## Example

```
{
  a: 1,
  b: 2.0,
  c: true,
  d: false,
  e: 'Hello, World!',
  f: "Hello, World!",
  g: {
    foo: {
      bar: "Boom!"
    }
  },
  h: .a.b.c,
  i: .a.b.foo(it -> {foo: it.foo, bar: it.bar}),
  j: .a.b.bar[it -> {foo: it.foo, bar: it.bar}],
  k: .a.b?.c,
  l: foobar.foo.bar
}
```

## JDK Proxy

DMP provides a strong type resolution

```Java
public interface User2PeopleMapper {

    @Dmp("{id: .userId, name: .username, nichname: .nickname, age: .age, friendNames: .friends[it->it?.name]}")
    People map(@Origin User user);
    
    class User {
        private Long userId;
        private String username;
        private String nickname;
        private Short age;
        private List<User> friends;
        // getters/setters
    }
    
    class People {
        private Long id;
        private String name;
        private String nickname;
        private Short age;
        private List<String> friendNames;
        // getters/setters
    }
}

User2PeopleMapper mapper = DmpProxy.newInstance(User2PeopleMapper.class, OBJECT_MAPPER::convertValue);
People people = mapper.map(user);
```

There is no naming conventions requirement here, the only requirement is all methods must be annotated with @DMP with script, the object meets the POJO requirements.
