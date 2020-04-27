# Nanoleaf Simulator
This is a simple simulator for the Nanoleaf Aurora and Canvas made to help developers. The simulator does not support api endpoints related to dynamic or rhythm effects. External streaming however <u>is</u> supported.

## Usage
The simulator can be interfaced with in exactly the same way as a physical Nanoleaf device through the [OpenAPI.](https://forum.nanoleaf.me/docs/openapi) However, the simulator will not broadcast it's address to other applications. The default <b>HTTP port for the simulator is 7144</b> and the default UDP (used for external streaming) port is 7143.

## Screenshots
![](images/app_main_aurora.png)

![](images/app_main_canvas.png)

## Changing the Layout
Right now, layouts are defined in files with the .nleaf extension in plain text that use the following format. You may define as many panels as you like.
```
type <Aurora/Canvas>
panel <id> <x> <y> <orientation>
panel <id> <x> <y> <orientation>
panel <id> <x> <y> <orientation>
...
```

If I ever get around to it I will probably add an editor feature to allow the panels to be added/removed and rearranged within the simulator.
