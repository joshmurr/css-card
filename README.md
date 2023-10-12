# 3D Interactive Cards in CSS

This is a port of [simeydotme](https://github.com/simeydotme)'s great [Pokémon Cards Holographic effect in CSS](https://github.com/simeydotme/pokemon-cards-css). It's not a full port, just enough for me to play around with a bit of Clojurescript and figure out the maths behind the rotation in 3D. Turns out it's all incredibly simple, of course. The XY position of the mouse just sets a rotation in degrees and then the CSS 3D engine handles the perspective.

I thought just updating `:root` variables in CSS was quite a neat way of doing it and keeping all the work in CSS. I used the [`pub`-`sub` model](https://github.com/clojure/core.async/wiki/Pub-Sub) from Clojure's `core.async` to asynchronously update the CSS vars. Took a minute to figure out the structure, but it seems pretty great once you do - a fairly tidy version of `context`+`reducer` in React. I'd argue the Clojure `pub`-`sub` model is better just because it's slightly less confusing. I've always found `context`+`reducer` confusing for some reason despite using it all the time. To be fair I might be comparing apples and oranges there, but they seem kinda similar to me.

And then putting spring animations on everything just makes it all smooth and wobbly. I haven't done that yet but I probably will.
