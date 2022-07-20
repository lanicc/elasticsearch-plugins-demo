package io.github.lanicc.plugin.search.query;

import org.apache.lucene.search.Query;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.index.query.QueryShardContext;
import org.elasticsearch.index.query.TermsQueryBuilder;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * Created on 2022/7/20.
 *
 * @author lan
 */
public class SpiltToTermsQueryBuilder extends AbstractQueryBuilder<SpiltToTermsQueryBuilder> {

    public static final String NAME = "spiltToTerms";

    private static final String SEPERATOR_COMMA = ",";

    private static final String SEPERATOR = "sep";

    private static final String FIELD = "field";

    private static final String TERMS = "terms";

    private TermsQueryBuilder termsQueryBuilder;

    private String sep;
    private String field;
    private String terms;

    public SpiltToTermsQueryBuilder(String field, String terms) {
        this.sep = SEPERATOR_COMMA;
        this.field = field;
        this.terms = terms;
    }

    public SpiltToTermsQueryBuilder(String sep, String field, String terms) {
        this.sep = sep;
        this.field = field;
        this.terms = terms;
        String[] strings = terms.split(sep);
        this.termsQueryBuilder = new TermsQueryBuilder(field, strings);
    }


    public SpiltToTermsQueryBuilder(StreamInput in) throws IOException {
        super(in);
        this.sep = in.readOptionalString();
        this.field = in.readString();
        this.terms = in.readString();
        String[] strings = terms.split(sep);
        this.termsQueryBuilder = new TermsQueryBuilder(field, strings);
    }

    @Override
    protected void doWriteTo(StreamOutput out) throws IOException {
        out.writeOptionalString(sep);
        out.writeString(field);
        out.writeString(terms);
    }

    @Override
    protected void doXContent(XContentBuilder builder, Params params) throws IOException {

    }

    @Override
    protected Query doToQuery(QueryShardContext context) throws IOException {
        return termsQueryBuilder.toQuery(context);
    }

    @Override
    protected boolean doEquals(SpiltToTermsQueryBuilder other) {
        return Objects.equals(termsQueryBuilder, other.termsQueryBuilder);
    }

    @Override
    protected int doHashCode() {
        return Objects.hash(sep, field, terms);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }

    public String getSep() {
        return sep;
    }

    public String getField() {
        return field;
    }

    public String getTerms() {
        return terms;
    }


    @SuppressWarnings("resource")
    public static Optional<QueryBuilder> fromXContent(QueryParseContext parseContext) throws IOException {
        XContentParser parser = parseContext.parser();

        String sep = null, field = null, terms = null, queryName = null;

        String currentFieldName = null;
        XContentParser.Token token;

        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.VALUE_STRING) {
                String value = parser.text();
                if (Objects.equals(currentFieldName, SEPERATOR)) {
                    sep = value;
                } else if (Objects.equals(currentFieldName, FIELD)) {
                    field = value;
                } else if (Objects.equals(currentFieldName, TERMS)) {
                    terms = value;
                } else if (AbstractQueryBuilder.NAME_FIELD.match(currentFieldName)) {
                    queryName = value;
                }
                currentFieldName = null;
            }
        }
        assert terms != null;
        SpiltToTermsQueryBuilder spiltToTermsQueryBuilder = new SpiltToTermsQueryBuilder(sep, field, terms);
        if (queryName != null) {
            spiltToTermsQueryBuilder.queryName(queryName);
        }

        return Optional.of(spiltToTermsQueryBuilder);
    }
}
