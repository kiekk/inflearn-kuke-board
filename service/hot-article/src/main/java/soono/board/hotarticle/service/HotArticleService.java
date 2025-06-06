package soono.board.hotarticle.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import soono.board.common.event.Event;
import soono.board.common.event.EventPayload;
import soono.board.common.event.EventType;
import soono.board.hotarticle.client.ArticleClient;
import soono.board.hotarticle.repository.HotArticleListRepository;
import soono.board.hotarticle.service.event.handler.EventHandler;
import soono.board.hotarticle.service.response.HotArticleResponse;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotArticleService {
    // redis
    // 게시글 원본 정보를 ArticleClient에서 조회 가능
    private final ArticleClient articleClient;
    private final List<EventHandler> eventHandlers;
    private final HotArticleScoreUpdater hotArticleScoreUpdater;
    private final HotArticleListRepository hotArticleListRepository;

    public void handleEvent(Event<EventPayload> event) {
        EventHandler<EventPayload> eventHandler = findEventHandler(event);

        if (eventHandler == null) {
            return;
        }

        if (isArticleCreatedOrDeleted(event)) {
            eventHandler.handle(event);
        } else {
            hotArticleScoreUpdater.update(event, eventHandler);
        }
    }

    public List<HotArticleResponse> readAll(String dateStr) {
        return hotArticleListRepository.readAll(dateStr).stream()
                .map(articleClient::read)
                .map(HotArticleResponse::from)
                .toList();
    }

    private EventHandler findEventHandler(Event<EventPayload> event) {
        return eventHandlers.stream()
                .filter(eventHandler -> eventHandler.supports(event))
                .findFirst()
                .orElse(null);
    }

    private boolean isArticleCreatedOrDeleted(Event<EventPayload> event) {
        return EventType.ARTICLE_CREATED == event.getType() ||
                EventType.ARTICLE_DELETED == event.getType();
    }

}
