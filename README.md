# AdrendlineJson

This is a Java library for manipulating JSON. There are many like it. This one is mine.


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

A `JsonArray` is simply a list of `JsonValue`s. You can store whatever you want in it.
```Java
JsonArray array = new JsonArray();
array.add(true);
array.add("two");
array.add(3);
array.add(new JsonArray());
array.add(new JsonObject());
System.out.println(array);
```
yield:
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
Note that the `Number` was automatically converted to a `String`.

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