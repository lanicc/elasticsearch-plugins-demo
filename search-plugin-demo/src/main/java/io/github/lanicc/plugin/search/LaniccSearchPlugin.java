package io.github.lanicc.plugin.search;

import io.github.lanicc.plugin.search.query.SpiltToTermsQueryBuilder;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.SearchPlugin;

import java.util.Collections;
import java.util.List;

/**
 * Created on 2022/7/20.
 *
 * @author lan
 */
public class LaniccSearchPlugin extends Plugin implements SearchPlugin {

    @Override
    public List<QuerySpec<?>> getQueries() {
        return Collections.singletonList(new QuerySpec<>(SpiltToTermsQueryBuilder.NAME, SpiltToTermsQueryBuilder::new, SpiltToTermsQueryBuilder::fromXContent));
    }
}
