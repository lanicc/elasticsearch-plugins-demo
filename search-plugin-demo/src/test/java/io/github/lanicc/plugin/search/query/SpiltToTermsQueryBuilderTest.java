package io.github.lanicc.plugin.search.query;

import org.elasticsearch.cluster.ClusterModule;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryParseContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created on 2022/7/20.
 *
 * @author lan
 */
class SpiltToTermsQueryBuilderTest {

    @SuppressWarnings("resource")
    @Test
    void fromXContent() throws IOException {
        XContentBuilder builder =
                jsonBuilder().startObject()
                        .field("sep", ",")
                        .field("field", "a")
                        .field("terms", "1,2,3,4,5")
                        .field("_name", "name")
                        .endObject();
        XContentParser parser = XContentType.JSON.xContent().createParser(new NamedXContentRegistry(ClusterModule.getNamedXWriteables()), builder.bytes());
        Optional<QueryBuilder> optional = SpiltToTermsQueryBuilder.fromXContent(new QueryParseContext(parser));
        assertTrue(optional.isPresent());
        SpiltToTermsQueryBuilder spiltToTermsQueryBuilder = (SpiltToTermsQueryBuilder) optional.get();
        assertEquals(",", spiltToTermsQueryBuilder.getSep());
        assertEquals("a", spiltToTermsQueryBuilder.getField());
        assertEquals("1,2,3,4,5", spiltToTermsQueryBuilder.getTerms());
    }
}
