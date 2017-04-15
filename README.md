#repeat

`repeat` is a java templating library that aims to be fast, reliable, lightweight, extensible.

##Download and dependecies

The only two runtime dependencies are `javassist` and `log4j`; building will require also `project lombok` and `junit`.

The build is done with the `maven` utility.

A prebuilt jar can be found under the `releases` section.

##Project overview

The classes in the `dgdevel.repeat.api` and sub-packages are considered stable.

To compile a template the needed stuff are a `Resolver` instance to retrive the source of the template in `String` form, a `Context` instance that will keep reference of all the generated templates, and invoke if needed the compiler, one or more `TokenHandler` instances that defines the syntax of the template.

A compiled `Template` instance will have two methods named `run`, that will take a `Map<String, Object>` of parameters named `params` and the output object named `out`, a `StringBuilder` or a `Writer`.

###Context

A `DefaultContext` class is provided under `dgdevel.repeat.impl`, even if it's not under the api package there will be made efforts to keep it stable.

###Resolvers

In the `dgdevel.repeat.predef.resolvers` can be found some useful `Resolver` implementation.

- `CascadeResolver`: it will receive a list of resolvers, and try them one after the other to get the sources

- `ClassLoaderResolver`: it will try to fetch the content of a resource via the `ClassLoader.getResourceAsStream` method

- `DirectoryResolver`: it will search in the specified directory for the named file

- `StringResolver`: it will keep only one template, name and source form, and return the source if that name is searched.

###TokenHandler

Under the `dgdevel.repeat.predef.tokenhandlers` package there are some useful implementation, to be used to extend the syntax.

###Presets

Two presets can be found under the `dgdevel.repeat.predef.syntax` package: a jsp like syntax and a mustache like syntax. See examples for usage.

###Examples
Under the examples directory more extensive examples can be found.
