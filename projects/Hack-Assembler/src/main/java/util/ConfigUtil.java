
package util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

//TODO TEST
public class ConfigUtil {

    private static class Pair<K,V> {
        private K first;
        private V second;
        private Pair(K first, V second) {
            this.first = first;
            this.second = second;
        }
        private static <EK,EV,K,V> Pair<K,V> fromEntry(Map.Entry<EK,EV> entry,Function<EK,K> keyMapper, Function<EV,V> valueMapper){
            return new Pair<K,V>(keyMapper.apply(entry.getKey()), valueMapper.apply(entry.getValue()));
        }
    }

    public static <E> Function<Object,List<E>>listMapper(Function<Object,E> valueMapper){
        return object -> ((List<E>) object)
                .stream()
                .map(valueMapper)
                .collect(Collectors.toList());
    }

    public static <K, V> Multimap<K, V> multimapFromConfig(String configName, Function<String, K> keyMapper, Function<Object, List<V>> valueMapper) {
        JSONObject jsonObject = parseJson(getPathForName(configName));
        Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
        return entries.stream()
                .map(entry -> Pair.fromEntry(entry,keyMapper,valueMapper))
                .collect(ArrayListMultimap::create,
                (multimap,pair) -> multimap.putAll(pair.first, pair.second),
                Multimap::putAll);
    }

    public static <K, V> Map<K, V> mapFromConfig(String configName, Function<String, K> keyMapper, Function<Object, V> valueMapper) {
        JSONObject jsonObject = parseJson(getPathForName(configName));
        Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
        return entries.stream().collect(Collectors.toMap(
                entry -> keyMapper.apply(entry.getKey()),
                entry -> valueMapper.apply(entry.getValue())));
    }

    public static BiMap<String, Integer> biMapFromConfig(String configName) {
        JSONObject jsonObject = parseJson(getPathForName(configName));
        BiMap<String, Integer> biMap = HashBiMap.create();
        jsonObject.forEach((string, longV) -> biMap.put(String.valueOf(string), Math.toIntExact((Long) longV)));
        return replaceNull(biMap);
    }

    private static Path getPathForName(String name) {
        return Paths.get("src/main/resources/" + name + ".json");
    }

    private static JSONObject parseJson(Path config) {
        checkNotNull(config);
        checkArgument(Files.exists(config), "The configuration file %s does not exist.", config.toAbsolutePath());
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) parser.parse(Files.newBufferedReader(config));
        } catch (ParseException e) {
            System.err.println(e);
            throw new RuntimeException("The config file " + config.toAbsolutePath().toString() + " is not valid JSON.", e);
        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException("Could not read config from " + config.toAbsolutePath().toString(), e);
        }

        return jsonObject;
    }

    private static <K, V> BiMap<K, V> replaceNull(BiMap<K, V> map) {
        V value = map.get("null");
        map.remove("null");
        map.put(null, value);
        return map;
    }
}
