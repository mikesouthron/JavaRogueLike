# JavaRogueLike

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/mikesouthron/JavaRogueLike/tree/master.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/mikesouthron/JavaRogueLike/tree/master)

![](https://i.imgur.com/FzwmyyE.png)

## Install

* Download the zip file from Releases for your platform.
* Extract the zip.
* In bin/ double click on javarl/javarl.bat (depending on platform)
* A command/shell window will appear and the game will launch.

## Controls

* Numpad keys to move, 8-way movement.
* Escape to quit.

## Dev Details

Adapting the Python-libtcod tutorial [here](https://rogueliketutorials.com/tutorials/tcod/v2/) directly to Java as much as possible.

Using the code from [AsciiPanel](https://github.com/trystan/AsciiPanel) to drive the basic UI.

No other external libraries.

Some of the algorithms are ported directly from the libtcod C code.

### [Bresenham Lines](src/main/java/org/southy/rl/gen/Bresenham.java)

Ported from [bresenham_c.c](https://github.com/libtcod/libtcod/blob/develop/src/libtcod/bresenham_c.c)

I converted the use of TCOD_bresenham_data_t into the Bresenham class itself holding it's own state.

I replicate the int* __restrict xCur, int* __restrict yCur reference params with a Position object that gets updated every step.

### [Field of View](src/main/java/org/southy/rl/map/FoVRestrictiveShadowCasting.java)

Ported from [fov_restrictive.c](https://github.com/libtcod/libtcod/blob/develop/src/libtcod/fov_restrictive.c)

This code is almost an exact replica with some type name changes.

I have left in place the ability to switch to a different FoV algorithm, but not sure if I will bother implementing them.
