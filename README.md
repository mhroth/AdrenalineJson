# AdrenalineJson

This is a Java library for manipulating JSON. There are many like it. This one is mine.

## Why use it?
 * It has a nice and clean interface.
 * It is small (23KB).
 * No external dependencies.
 * Compatible with Java 1.6 and above.
 * Parses and produces standard JSON. No funny stuff.
 * [BSD license](http://www.w3.org/Consortium/Legal/2008/03-bsd-license.html). Just use it. I don't care what you do with it.

### Why not use it?
  * It doesn't convert arbitrary Java objects into JSON. [[Gson](http://code.google.com/p/google-gson/)]
  * It hasn't been optimised for speed. That's not to say that it's slow. It's just that I haven't profiled it, and it's not a priority. [[Json-Smart](http://code.google.com/p/json-smart/)]
  * It doesn't decode or encode directly with streams.
  * It doesn't have anything to do with XML or SAX. [[Jackson](http://jackson.codehaus.org/)]

AdrenalineJson doesn't have all of the features of other libraries, but that's ok because you probably aren't using them anyway. Adrenaline goes in, gets the job done as quickly as possible, and leaves you to get on with things.


# Examples

## JsonObject

A `JsonObject` implements the `Map<String,JsonValue>` interface. Easy to use!

```Java
JsonObject obj = new JsonObject();
obj.put("hello", "world");
obj.put("response", 42);
obj.put("byebye", false);
obj.put("otherobj", new JsonObject());
System.out.println(obj);
```
yields:
```
{"hello":"world","response":42,"byebye":false,"otherobj":{}}
```
Of course it does pretty print too.
```Java
System.out.println(obj.toString(2));
```
```Java
{
  "hello": "world",
  "response": 42,
  "byebye": false,
  "otherobj": {}
}  
```

Normally when retrieving values from the map, a `JsonValue` object is returned. This can be annoying if you already know what the object type is.
```Java
String str = obj.getString("hello");
double d = obj.getNumber("response").doubleValue();
boolean b = obj.get("byebye").asBoolean();
```
You can even dynamially cast `JsonValue`s as needed.
```Java
boolean b = obj.get("byebye").asBoolean();
String str = obj.get("byebye").asString();
System.out.println(str);
```
yields:
```Java
false
```

## JsonArray

A `JsonArray` is simply a list of `JsonValue`s. Really, it implements the `List<JsonValue>` interface. Use it wherever you use `List`s.
```Java
JsonArray array = new JsonArray();
array.add(true);
array.add("two");
array.add(3);
array.add(new JsonArray());
array.add(new JsonObject());
System.out.println(array);
```
yields:
```Java
[true,"two",3,[],{}]
```

But most of the time there is only type of object in the array, and you know what is.
```Java
JsonArray array = new JsonArray();
array.add("one");
array.add("two");
array.add("three");
array.add(4.0);

for (String s : array.stringIterable()) {
  System.out.println(s);
}
```
yields:
```Java
one
two
three
4
```
Note that the `Number` was automatically converted to a `String`. This kind of a cast can result in a `JsonCastException`. It is a `RuntimeException`, so it doesn't need to caught unless you plan on doing something stupid.

## Parsing

```Java
String jsonString = "{\"hello\":\"world\"}";
JsonValue value = JsonObject.parse(jsonString);
JsonObject jsonObj = value.asMap();
for (Map.Entry<String,JsonValue> e : jsonObj.entrySet()) {
  System.out.println(e.getKey + ": " + e.getValue());
}
```
yields:
```Java
hello: world
```
`JsonObject.parse(...)` can throw a `JsonParseException`, but it is a `RuntimeException` and it isn't necessary to catch it if you don't expect anything bad.

# Miscellaneous

## Open Source License

AdrenalineJson is open source under the [BSD 3-Clause License](http://www.w3.org/Consortium/Legal/2008/03-bsd-license.html).

## Known Bugs
 * Characters `[`, `]`, `{`, and `}` inside of strings will break the parsing.