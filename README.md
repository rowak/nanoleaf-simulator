# Nanoleaf Simulator
This is a simple simulator for the Nanoleaf Aurora and Canvas made to help developers. The simulator does not support api endpoints related to dynamic or rhythm effects.

## Changing the Layout
Right now, layouts are defined in files with the .nleaf extension in plain text that use the following format. You may define as many panels as you like.
<code>
type &lt;Aurora/Canvas&gt;
panel &lt;id&gt; &lt;x&gt; &lt;y&gt; &lt;orientation&gt;
panel &lt;id&gt; &lt;x&gt; &lt;y&gt; &lt;orientation&gt;
panel &lt;id&gt; &lt;x&gt; &lt;y&gt; &lt;orientation&gt;
...
</code>

If I ever get around to it I will probably add an editor feature to allow the panels to be added/removed and rearranged within the simulator.
