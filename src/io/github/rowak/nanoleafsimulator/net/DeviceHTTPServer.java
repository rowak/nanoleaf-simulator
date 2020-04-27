package io.github.rowak.nanoleafsimulator.net;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import fi.iki.elonen.NanoHTTPD;
import io.github.rowak.nanoleafapi.Panel;
import io.github.rowak.nanoleafsimulator.simulators.Simulator;
import io.github.rowak.nanoleafsimulator.tools.ApiKeyManager;

public class DeviceHTTPServer extends NanoHTTPD
{
	private int udpPort;
	private List<String> accessTokens;
	private Simulator simulator;
	
	public DeviceHTTPServer(Simulator simulator,
			int httpPort, int udpPort) throws IOException
	{
		super(httpPort);
		this.simulator = simulator;
		this.udpPort = udpPort;
		accessTokens = new ArrayList<String>();
		readAccessTokens();
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
	}
	
	@Override
    public Response serve(IHTTPSession session)
    {
		String uri = session.getUri().substring(1);
		String[] endpoint = uri.split("/");
		// regular api calls
		if (endpoint.length > 2 && endpoint[0].equals("api") &&
				endpoint[1].equals("v1") && accessTokens.contains(endpoint[2]))
		{
			if (endpoint.length > 3)
			{
				// GET api calls
				if (session.getMethod() == Method.GET)
				{
					switch (endpoint[3])
					{
						case "state":
							switch (endpoint[4])
							{
								case "on":
									if (endpoint.length > 5 && endpoint[5].equals("value"))
									{
										return newFixedLengthResponse(Response.Status.OK,
												"application/json", simulator.getOn() + "");
									}
									return newFixedLengthResponse(Response.Status.OK, "application/json",
											"{\"value\":" + simulator.getOn() + "}");
								case "brightness":
									if (endpoint.length > 5 && endpoint[5].equals("value"))
									{
										return newFixedLengthResponse(Response.Status.OK,
												"application/json", simulator.getBrightness() + "");
									}
									return newFixedLengthResponse(Response.Status.OK, "application/json",
											"{\"value\":" + simulator.getBrightness() +
											",\"max\":100,\"min\":0}");
								case "hue":
									if (endpoint.length > 5 && endpoint[5].equals("value"))
									{
										return newFixedLengthResponse(Response.Status.OK,
												"application/json", simulator.getHue() + "");
									}
									return newFixedLengthResponse(Response.Status.OK, "application/json",
											"{\"value\":" + simulator.getHue() +
											",\"max\":360,\"min\":0}");
								case "sat":
									if (endpoint.length > 5 && endpoint[5].equals("value"))
									{
										return newFixedLengthResponse(Response.Status.OK,
												"application/json", simulator.getSaturation() + "");
									}
									return newFixedLengthResponse(Response.Status.OK, "application/json",
											"{\"value\":" + simulator.getSaturation() +
											",\"max\":100,\"min\":0}");
								case "ct":
									if (endpoint.length > 5 && endpoint[5].equals("value"))
									{
										return newFixedLengthResponse(Response.Status.OK,
												"application/json", simulator.getColorTemperature() + "");
									}
									return newFixedLengthResponse(Response.Status.OK, "application/json",
											"{\"value\":" + simulator.getColorTemperature() +
											",\"max\":6500,\"min\":1200}");
							}
							break;
						case "effects":
							if (endpoint.length > 4)
							{
								switch (endpoint[4])
								{
									case "select":
										return newFixedLengthResponse(Response.Status.OK,
												"application/json", simulator.getSelectedEffect());
									case "effectsList":
										return newFixedLengthResponse(Response.Status.OK,
												"application/json",
												Arrays.asList(simulator.getEffectsList()) + "");
								}
							}
							return newFixedLengthResponse(Response.Status.OK,
									"application/json", "{\"effectsList\":" +
											Arrays.asList(simulator.getEffectsList()) +
											",\"selected\":\"" + simulator.getSelectedEffect() + "\"}");
						case "panelLayout":
							switch (endpoint[4])
							{
								case "layout":
									StringBuilder sb = new StringBuilder();
									Panel[] panels = simulator.getPanels();
									sb.append("{\"numPanels\":" + panels.length +
											",\"positionData\":[");
									for (int i = 0; i < panels.length; i++)
									{
										Panel p = panels[i];
										sb.append("{\"panelId\":" + p.getId() +
												  ",\"x\":" + p.getX() +
												  ",\"y\":" + p.getY() +
												  ",\"o\":" + p.getOrientation() + "}");
										if (i < panels.length)
										{
											sb.append(",");
										}
									}
									sb.append("]}");
									return newFixedLengthResponse(Response.Status.OK,
											"application/json", sb.toString());
								case "globalOrientation":
									if (endpoint.length > 5 && endpoint[5].equals("value"))
									{
										return newFixedLengthResponse(Response.Status.OK,
												"application/json", simulator.getGlobalOrientation() + "");
									}
									String response = "{\"value\":" +
											simulator.getGlobalOrientation() +
											",\"max\":360,\"min\":0}";
									return newFixedLengthResponse(Response.Status.OK,
											"application/json", response);
							}
							break;
					}
				}
				// PUT api calls
				else if (session.getMethod() == Method.PUT)
				{
					// Source: https://stackoverflow.com/questions/22349772/retrieve-http-body-in-nanohttpd
					Integer contentLength = Integer.parseInt(session.getHeaders().get("content-length"));
					byte[] buffer = new byte[contentLength];
					try
					{
						session.getInputStream().read(buffer, 0, contentLength);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					JSONObject body = new JSONObject(new String(buffer));
					switch (endpoint[3])
					{
						case "state":
							String state = null;
							for (String k : body.keySet())
							{
								state = k;
							}
							switch (state)
							{
								case "on":
									simulator.setOn(body.getJSONObject("on").getBoolean("value"));
									return newFixedLengthResponse(Response.Status.NO_CONTENT,
											"application/json", "");
								case "brightness":
									simulator.setBrightness(body.getJSONObject("brightness").getInt("value"));
									return newFixedLengthResponse(Response.Status.NO_CONTENT,
											"application/json", "");
								case "hue":
									simulator.setHue(body.getJSONObject("hue").getInt("value"));
									return newFixedLengthResponse(Response.Status.NO_CONTENT,
											"application/json", "");
								case "sat":
									simulator.setSaturation(body.getJSONObject("sat").getInt("value"));
									return newFixedLengthResponse(Response.Status.NO_CONTENT,
											"application/json", "");
								case "ct":
									simulator.setColorTemperature(body.getJSONObject("ct").getInt("value"));
									return newFixedLengthResponse(Response.Status.NO_CONTENT,
											"application/json", "");
							}
							break;
						case "effects":
							if (endpoint.length > 4)
							{
								String effect = null;
								for (String k : body.keySet())
								{
									effect = k;
								}
								switch (effect)
								{
									case "select":
										String selected = body.getString("select");
										simulator.setSelectedEffect(selected);
										return newFixedLengthResponse(Response.Status.NO_CONTENT,
												"application/json", "");
									case "write":
										break;
								}
							}
							else if (body.has("write"))
							{
								JSONObject write = body.getJSONObject("write");
								if (write.getString("command").equals("display") &&
										write.getString("animType").equals("extControl"))
								{
									simulator.startUDPServer();
									return newFixedLengthResponse(Response.Status.OK,
											"application/json", "{\"streamControlIpAddr\":" +
											"\"localhost\",\"streamControlPort\":" + udpPort +
											",\"streamControlProtocol\":\"udp\"}");
								}
								return newFixedLengthResponse(Response.Status.NO_CONTENT,
										"application/json", "");
							}
							break;
						case "panelLayout":
							String layout = null;
							for (String k : body.keySet())
							{
								layout = k;
							}
							switch (layout)
							{
								case "globalOrientation":
									int degrees = body.getJSONObject(
											layout).getInt("value");
									if (degrees >= 0 && degrees <= 360)
									{
										simulator.setGlobalOrientation(degrees);
										return newFixedLengthResponse(Response.Status.NO_CONTENT,
												"application/json", "");
									}
									return newFixedLengthResponse(Response.Status.BAD_REQUEST,
											"application/json", "");
							}
							break;
					}
				}
			}
			// get all device info
			else if (endpoint.length == 3)
			{
				String response = "{\"name\":\"" + simulator.getName() +
						"\",\"serialNo\":\"" + simulator.getSerialNo() +
						"\",\"manufacturer\":\"" + simulator.getManufacturer() +
						"\",\"firmwareVersion\":\"" + simulator.getFirmwareVersion() +
						"\",\"hardwareVersion\":\"" + simulator.getHardwareVersion() +
						"\",\"model\":\"" + simulator.getModel() + "\"}";
				return newFixedLengthResponse(Response.Status.OK, "application/json", response);
			}
		}
		// create new access token
		else if (endpoint[0].equals("api") && endpoint[1].equals("v1") &&
				endpoint[2].equals("new") && session.getMethod() == Method.POST)
		{
			final String accessToken = getNewAccessToken();
			accessTokens.add(accessToken);
			ApiKeyManager.addKey(accessToken);
			System.out.println(accessToken);
			return newFixedLengthResponse(Response.Status.OK, "application/json",
					"{\"auth_token\":\"" + accessToken + "\"}");
		}
		return newFixedLengthResponse(Response.Status.NOT_FOUND, "application/json", "{}");
    }
	
	private String getNewAccessToken()
	{
		Random random = new Random();
		String token = "";
		for (int i = 0; i < 32; i++)
		{
			if (random.nextBoolean())
			{
				token += (char)(random.nextInt(90-65+1)+65);
			}
			else
			{
				token += (char)(random.nextInt(122-97+1)+97);
			}
		}
		return token;
	}
	
	private void readAccessTokens()
	{
		for (String key : ApiKeyManager.getKeys())
		{
			accessTokens.add(key);
		}
	}
}
