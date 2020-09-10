
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

//TODO TEST
public class ConfigUtil {

    public static final String RESOURCES_DIR = "src/main/resources/";

    public static <E> Function<Object, List<E>> listMapper(Function<Object, E> valueMapper) {
        return object -> ((List<E>) object)
                .stream()
                .map(valueMapper)
                .collect(Collectors.toList());
    }

    public static <K, V> Multimap<K, V> multimapFromConfig(String configName, Function<String, K> keyMapper, Function<Object, List<V>> valueMapper) {
        Map<K, List<V>> listMap = mapFromConfig(configName, keyMapper, valueMapper);
        Multimap<K, V> mmap = ArrayListMultimap.create();
        listMap.forEach(mmap::putAll);
        return mmap;
    }

    public static <K, V> Map<K, V> mapFromConfig(String configName, Function<String, K> keyMapper, Function<Object, V> valueMapper) {
        return fromConfig(configName, keyMapper, valueMapper,
                HashMap::new,
                (map, pair) -> map.put(pair.first, pair.second),
                HashMap::putAll);
    }

    public static BiMap<String, Integer> biMapFromConfig(String configName) {
        return replaceNull(biMapFromConfig(configName, s -> s, o -> Math.toIntExact((Long) o)));
    }

    public static <K, V> BiMap<K, V> biMapFromConfig(String configName,
                                                     Function<String, K> keyMapper, Function<Object, V> valueMapper) {
        return fromConfig(configName, keyMapper, valueMapper,
                HashBiMap::create,
                (biMap, pair) -> biMap.put(pair.first, pair.second),
                HashBiMap::putAll);
    }

    public static <M extends Map<K, V>, K, V> M fromConfig(String configName,
                                                           Function<String, K> keyMapper, Function<Object, V> valueMapper,
                                                           Supplier<M> supplier, BiConsumer<M, Pair<K, V>> accumulator, BiConsumer<M, M> combiner) {
        checkNotNull(configName);
        checkNotNull(keyMapper);
        checkNotNull(valueMapper);
        checkNotNull(supplier);
        checkNotNull(accumulator);
        checkNotNull(combiner);

        JSONObject jsonObject = parseJson(getPathForName(configName));
        Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
        return entries.stream()
                .map(entry -> Pair.fromEntry(entry, keyMapper, valueMapper))
                .collect(supplier, accumulator, combiner);
    }

    private static Path getPathForName(String name) {
        return Paths.get(RESOURCES_DIR + name + ".json");
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

    private static class Pair<K, V> {
        private final K first;
        private final V second;

        private Pair(K first, V second) {
            this.first = first;
            this.second = second;
        }

        private static <EK, EV, K, V> Pair<K, V> fromEntry(Map.Entry<EK, EV> entry, Function<EK, K> keyMapper, Function<EV, V> valueMapper) {
            return new Pair<K, V>(keyMapper.apply(entry.getKey()), valueMapper.apply(entry.getValue()));
        }
    }

    private static <K, V> BiMap<K, V> replaceNull(BiMap<K, V> map) {
        V value = map.get("null");
        map.remove("null");
        map.put(null, value);
        return map;
    }
}
