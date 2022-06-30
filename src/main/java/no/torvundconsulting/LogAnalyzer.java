package no.torvundconsulting;

import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class LogAnalyzer {
    public static void main(String[] args) {
        Gson gson = new Gson();
        List<String> kundeEntries = LogParsers.readJsonFromCsvColumn(
                "src/main/resources/kunde_search_7_days.csv",
                "_raw",
                "Entity: ");
        System.out.println("Kunde-entries: " + kundeEntries.size());

        System.out.println();
        System.out.println();
        List<CustomerEntity> customerEntities = kundeEntries.stream()
                .filter(s -> ! s.equals("{}"))
                .map(s -> gson.fromJson(s, CustomerEntity.class))
                .toList();
        final ConcurrentMap<SearchParam, AtomicLong> distribution = new ConcurrentHashMap<>();
        customerEntities.forEach(customerEntity -> {
            for (SearchParam searchParam : SearchParam.values()) {
                if (customerEntity.isNotEmpty(searchParam.name())) {
                    distribution.putIfAbsent(searchParam, new AtomicLong(0));
                    distribution.get(searchParam).incrementAndGet();
                }
            }
        });
        distribution.forEach((searchParam, atomicLong) -> System.out.println(searchParam + ", " + atomicLong));

        System.out.println();
        System.out.println();
        List<CustomerEntity> noKundenummer = customerEntities
                .stream()
                .filter(c -> c.getKundenummer() == null || c.getKundenummer().length == 0)
                .toList();
        final ConcurrentMap<SearchParam, AtomicLong> distribution2 = new ConcurrentHashMap<>();
        noKundenummer.forEach(customerEntity -> {
            for (SearchParam searchParam : SearchParam.values()) {
                if (customerEntity.isNotEmpty(searchParam.name())) {
                    distribution2.putIfAbsent(searchParam, new AtomicLong(0));
                    distribution2.get(searchParam).incrementAndGet();
                }
            }
        });
        distribution2.forEach((searchParam, atomicLong) -> System.out.println(searchParam + ", " + atomicLong));

        List<String> selskapEntries = LogParsers.readJsonFromCsvColumn(
                "src/main/resources/selskap_7_days.csv",
                "_raw",
                "Entity: ");
        System.out.println("Selskap-entries: " + selskapEntries.size());

        System.out.println();
        System.out.println();
        List<SelskapEntity> selskapEntities = selskapEntries.stream()
                .filter(s -> ! s.equals("{}"))
                .map(s -> gson.fromJson(s, SelskapEntity.class))
                .toList();
        final ConcurrentMap<SelskapSearchParam, AtomicLong> distribution3 = new ConcurrentHashMap<>();
        selskapEntities.forEach(selskapEntity -> {
            for (SelskapSearchParam searchParam : SelskapSearchParam.values()) {
                if (selskapEntity.isNotEmpty(searchParam.name())) {
                    distribution3.putIfAbsent(searchParam, new AtomicLong(0));
                    distribution3.get(searchParam).incrementAndGet();
                }
            }
        });
        distribution3.forEach((searchParam, atomicLong) -> System.out.println(searchParam + ", " + atomicLong));

    }
}
