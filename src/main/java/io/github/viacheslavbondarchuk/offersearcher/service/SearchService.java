package io.github.viacheslavbondarchuk.offersearcher.service;

import io.github.viacheslavbondarchuk.offersearcher.bean.ApplicationInfoBean;
import io.github.viacheslavbondarchuk.offersearcher.consumer.impl.EventKafkaConsumer;
import io.github.viacheslavbondarchuk.offersearcher.consumer.impl.MarketKafkaConsumer;
import io.github.viacheslavbondarchuk.offersearcher.consumer.impl.SelectionKafkaConsumer;
import io.github.viacheslavbondarchuk.offersearcher.domain.SearchRequest;
import io.github.viacheslavbondarchuk.offersearcher.domain.SearchResponse;
import io.github.viacheslavbondarchuk.offersearcher.domain.SearchResult;
import io.github.viacheslavbondarchuk.offersearcher.domain.SearchServiceStatus;
import io.github.viacheslavbondarchuk.offersearcher.exception.SearchServiceNotReadyException;
import io.github.viacheslavbondarchuk.offersearcher.util.Checking;
import io.github.viacheslavbondarchuk.offersearcher.util.KeyValuePair;
import io.github.viacheslavbondarchuk.offersearcher.util.QueryUtil;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService implements StatusAwareService<SearchServiceStatus> {
    private final EventStorage eventStorage;
    private final MarketStorage marketDocumentStorage;
    private final SelectionStorage selectionStorage;

    private final EventUpdateStorage eventUpdateStorage;
    private final MarketUpdateStorage marketUpdateStorage;
    private final SelectionUpdateStorage selectionUpdateStorage;

    private final EventKafkaConsumer eventKafkaConsumer;
    private final MarketKafkaConsumer marketKafkaConsumer;
    private final SelectionKafkaConsumer selectionKafkaConsumer;

    private final ApplicationInfoBean infoBean;

    @Override
    public KeyValuePair<String, SearchServiceStatus> getStatus() {
        return KeyValuePair.of(getServiceName(), new SearchServiceStatus(isReady()));
    }

    @Override
    public String getServiceName() {
        return "Search service";
    }

    private boolean isReady() {
        return eventKafkaConsumer.isReady() && marketKafkaConsumer.isReady() && selectionKafkaConsumer.isReady();
    }

    private SearchResult<List<Document>> internalSearch(AbstractDocumentStorage storage, SearchRequest request) {
        Checking.check(!isReady(), () -> new SearchServiceNotReadyException("Search service is not ready. For detail info call status endpoint"));
        return SearchResult.of(storage.count(QueryUtil.countQuery(request)), storage.find(QueryUtil.searchQuery(request)));
    }

    public SearchResponse<List<Document>> search(SearchRequest request) {
        SearchResult<List<Document>> searchResult = switch (request.getType()) {
            case EVENT -> internalSearch(eventStorage, request);
            case MARKET -> internalSearch(marketDocumentStorage, request);
            case SELECTION -> internalSearch(selectionStorage, request);
        };
        return new SearchResponse<>(searchResult.getResult(), searchResult.getHits(), request.getSkip(), request.getLimit(), infoBean.getBrandId());
    }

    public SearchResponse<List<Document>> searchUpdates(SearchRequest request) {
        SearchResult<List<Document>> searchResult = switch (request.getType()) {
            case EVENT -> internalSearch(eventUpdateStorage, request);
            case MARKET -> internalSearch(marketUpdateStorage, request);
            case SELECTION -> internalSearch(selectionUpdateStorage, request);
        };
        return new SearchResponse<>(searchResult.getResult(), searchResult.getHits(), request.getSkip(), request.getLimit(), infoBean.getBrandId());
    }


}
