# porkostomus/graph

## Rationale

I have a theory. And a secret.

### Theory

I really, really love working with SVG in cljs and I think it's a potential superpower. Since it is so easy to manipulate data in Clojure, and SVG is just declarative data, I believe that it can be 100% feasible to implement many ad-hoc graphical solutions for which would be commonly thought you'd need to reach for a library like graphviz.

### Secret

I actually had so much of a pain trying to get GraphViz to work in Reagent that I gave up and decided to just do my own thing by reverse-engineering their SVG output.

I can also now attempt to mentally justify this decision by telling myself that it would have been stupid to compile a C++ program written in the 1970s into JavaScript with Emscripten, just to render a few shapes.

## Development

Do the thing:

```bash
$ npm install
added 97 packages from 106 contributors in 5.984s
```

Start the development process by running:

```bash
$ npx shadow-cljs watch app
...
[:app] Build completed. (134 files, 35 compiled, 0 warnings, 5.80s)
```

Or simply `jack-in` from your editor. Your app will be served at: at [http://localhost:8080](http://localhost:8080).

## Production build

```bash
npx shadow-cljs release app
```
