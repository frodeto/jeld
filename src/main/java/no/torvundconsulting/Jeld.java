package no.torvundconsulting;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.google.gson.Gson;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.*;
import java.util.List;
import java.util.Random;

public class Jeld {

    public static void main(String[] args) {
        Gson gson = new Gson();
        try (FileWriter fw = new FileWriter("jeld.json", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fw)) {
            for (int i = 0; i < 100; i++) {
                bufferedWriter.write(gson.toJson(Ad.buildRandomAd()));
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        try (RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200)).build()) {
            ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
            ElasticsearchClient esClient = new ElasticsearchClient(transport);

            boolean created = createIndex(esClient);
            if (!created) {
                System.out.println("Could not create index");
                System.exit(-1);
            }

            try (InputStream input = new FileInputStream("/jeld.json")) {
                CreateIndexRequest req = CreateIndexRequest.of(b -> b
                        .index("ad-index")
                        .withJson(input)
                );
            }

            try(FileReader file = new FileReader(new File("/Users/frodetorvund/dev/jeld", "jeld.json"));) {
                IndexRequest<JsonData> req;

                req = IndexRequest.of(b -> b
                        .index("ad-index")
                        .withJson(file)
                );
                esClient.index(req);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    static boolean createIndex(ElasticsearchClient esClient) {
        boolean created = false;
        try(InputStream input = Jeld.class.getResourceAsStream("ad-index.json");) {
            CreateIndexRequest req = CreateIndexRequest.of(b -> b
                    .index("some-index")
                    .withJson(input)
            );

            created = esClient.indices().create(req).acknowledged();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return created;
    }

    private static void reCreateFile(String fileName) throws IOException {
        try (FileWriter fw = new FileWriter(fileName, false); // overwrite
             BufferedWriter bufferedWriter = new BufferedWriter(fw)) {
            bufferedWriter.write(";");
            bufferedWriter.newLine();
        }
    }

    private record Ad(
            String key,
            String type,
            String h1,
            String v1) {

        static Ad buildRandomAd() {
            Random rand = new Random();
            return new Ad(
                    keys.get(rand.nextInt(keys.size())),
                    types.get(rand.nextInt(keys.size())),
                    h1s.get(rand.nextInt(keys.size())),
                    v1s.get(rand.nextInt(keys.size())));
        }
    }

    static List<String> keys = List.of("Til Salgs", "Solgt", "Forkjøp");
    static List<String> types = List.of("Enebolig", "Rekkehus", "Leilighet");
    static List<String> h1s = List.of("Boareal", "Brutto", "netto");
    static List<String> v1s = List.of("Kjøkken", "Stue", "Soverom");

}
