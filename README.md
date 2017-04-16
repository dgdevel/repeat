# repeat

`repeat` is a java templating library that aims to be fast, reliable, lightweight, extensible.

## Download and dependecies

The only two runtime dependencies are `javassist` and `log4j`; building will require also `project lombok` and `junit`.

The build is done with the `maven` utility.

A prebuilt jar can be found under the `releases` section.

## Project overview

The classes in the `dgdevel.repeat.api` and sub-packages are considered stable.

To compile a template the needed stuff are a `Resolver` instance to retrive the source of the template in `String` form, a `Context` instance that will keep reference of all the generated templates, and invoke if needed the compiler, one or more `TokenHandler` instances that defines the syntax of the template.

A compiled `Template` instance will have two methods named `run`, that will take a `Map<String, Object>` of parameters named `params` and the output object named `out`, a `StringBuilder` or a `Writer`.

### Context

A `DefaultContext` class is provided under `dgdevel.repeat.impl`, even if it's not under the api package there will be made efforts to keep it stable.

### Resolvers

In the `dgdevel.repeat.predef.resolvers` can be found some useful `Resolver` implementation.

- `CascadeResolver`: it will receive a list of resolvers, and try them one after the other to get the sources

- `ClassLoaderResolver`: it will try to fetch the content of a resource via the `ClassLoader.getResourceAsStream` method

- `DirectoryResolver`: it will search in the specified directory for the named file

- `StringResolver`: it will keep only one template, name and source form, and return the source if that name is searched.

### TokenHandler

Under the `dgdevel.repeat.predef.tokenhandlers` package there are some useful implementation, to be used to extend the syntax.

Due to the parser simplicity they are evaluated in the order in which they appears, using only the prefix string. If an ambiguity arise the longest match takes precedence. For example defining both `<% .. %>` and `<%= .. %>` will match the latter. Then it will proceed on finding the closing tag, and if found it will call the `accept` method passing the content string; if it fails to accept a `ParseException` will be thrown.

### Presets

Two presets can be found under the `dgdevel.repeat.predef.syntax` package: a jsp like syntax and a mustache like syntax.

#### Jsp like syntax

Example source in jsp like syntax:
```
<!-- the variable will be emitted as is -->
Hello <%= params.get("name") %>

<!-- the variable will be xml-escaped -->
Hello <%- params.get("name") %>

<!-- java source -->
<% for (int i = 1; i <= 3; i++) { %>

    <!-- param object -->
    <% params.put("master.param", "" + i) %>

    <!-- include sub-templates -->
    <%< child param=master.param %>

<% } %>
```

And the code to run the example:

```
Resolver resolver = new ClassLoaderResolver(
	// a classloader can be passed, if null
	// the context class loader will be used
	Thread.currentThread().getContextClassLoader(),
	// the base package to search for templates
	"dgdevel.repeat.examples.jsplike",
	// the template extension
	"template",
	// the file charset
	Charset.forName("UTF-8")
);
// get a predefined context, with loaded syntax
Context context = JspLike.getExtendedContext(resolver);
// get or compile the named template
Template template = context.get("master");
// run the template
Map<String, Object> params = new HashMap<String, Object>();
params.put("name", "earth & moon");
StringBuilder out = new StringBuilder();
template.run(params, out);
// resulting output
System.out.println(out);
```

#### Mustache like syntax

Example source in mustache like syntax:

```
<!-- the variable will be xml-escaped -->
Hello {{ name }}

<!-- the variable will be emitted as is -->
Hello {{{ name }}}

<!-- java source -->
{{# for (int i = 1; i <= 3; i++) { }}

	<!-- include sub-templates -->
	{{> child param=name }}

{{# } }}
```

And the code to run the example:

```
Resolver resolver = new ClassLoaderResolver(
	// a classloader can be passed, if null
	// the context class loader will be used
	Thread.currentThread().getContextClassLoader(),
	// the base package to search for templates
	"dgdevel.repeat.examples.mustachelike",
	// the template extension
	"template",
	// the file charset
	Charset.forName("UTF-8")
);
// get a predefined context, with loaded syntax
Context context = MustacheLike.getExtendedContext(resolver);
// get or compile the named template
Template template = context.get("master");
// run the template
Map<String, Object> params = new HashMap<String, Object>();
params.put("name", "earth & moon");
StringBuilder out = new StringBuilder();
template.run(params, out);
// resulting output
System.out.println(out);
```

## License

`repeat` is released under the terms of GNU Lesser General Public License.
