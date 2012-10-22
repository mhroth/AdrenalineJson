# AdrendlineJson

This is a Java library for manipulating JSON. There are many like it. This one is mine.


# Examples

## JSONObject

A @JSONObject@ implements the @Map@ interface. Easy to use!

```Java
JSONObject obj = new JSONObject();
obj.put("hello", "world");
obj.put("response", 42);
obj.put("byebye", false);
System.out.println(obj);
```

```
{"hello":"world","response":42,"byebye":false}
```