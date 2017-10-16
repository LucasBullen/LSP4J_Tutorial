package org.eclipsecon.exercise4;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class EclipseConMap {

	public static final EclipseConMap INSTANCE = new EclipseConMap();
	
	final Properties props = new Properties();
	final Set<String> all;
	final Map<String, Collection<String>> startsFrom;
	final Map<String, Collection<String>> arrivesTo;
	final Map<String, String> type;
	
	private EclipseConMap() {
		InputStream propertiesStream = EclipseConMap.class.getResourceAsStream("/" + EclipseConMap.class.getPackage().getName().replace(".", "/") + "/chamrousseMap.properties");
		try {
			props.load(propertiesStream);
			propertiesStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.all = props.keySet().stream()
			.map(key -> ((String)key).split("\\.")[0])
			.collect(Collectors.toSet());
		this.startsFrom = props.entrySet().stream()
			.filter(entry -> ((String)entry.getKey()).endsWith(".startsFrom"))
			.collect(Collectors.toMap(
					entry -> ((String)entry.getKey()).split("\\.")[0],
					entry -> Arrays.asList(((String)entry.getValue()).split(","))));
		this.arrivesTo = new HashMap<String, Collection<String>>();
		this.startsFrom.forEach((arrival, starts) -> {
			starts.stream().forEach(start -> {
				Collection<String> arrivals = arrivesTo.get(start);
				if (arrivals == null) {
					arrivals = new HashSet<>();
				}
				arrivals.add(arrival);
				arrivesTo.put(start, arrivals);
			});
		});
		this.type = props.entrySet().stream()
			.filter(entry -> ((String)entry.getKey()).endsWith(".type"))
			.collect(Collectors.toMap(
					entry -> ((String)entry.getKey()).split("\\.")[0],
					entry -> (String)entry.getValue()));
	}

	/**
	 * @return the list of routes that link from with to
	 */
	public List<String> findWaysBetween(String from, String to) {
		List<String> res = new ArrayList<String>();
		for (String way : all) {
			if (startsFrom.get(way) != null && startsFrom.get(way).contains(from) &&
				arrivesTo.get(way) != null && arrivesTo.get(way).contains(to)) {
				res.add(way);
			}
		}
		return res;
	}
	
	public boolean startsFrom(String route, String potentialStart) {
		return this.startsFrom.get(route) != null && this.startsFrom.get(route).contains(potentialStart);
	}
}
