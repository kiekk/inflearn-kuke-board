### 게시글 단건 조회 최적화

- 게시글 조회 모델을 다음과 같이 설계해볼 수 있다.
  - 게시글 정보
  - 댓글 수
  - 좋아요 수
- 왜 게시글 조회 모델에 조회수 정보는 추가하지 않았을까?

- 조회수는 읽기 트래픽에 따라서 올라가는 데이터 특성
- 읽기 트래픽에 의해 쓰기 트래픽도 발생할 수 있는 상황
  - 조회수의 변경 마다 Query Model을 만드는게 오히려 비효율적일 수 있음
  - 조회수는 조회수 서비스에서도 빠른 저장소(Redis)에 이미 저장되어 있음
  - 조회수 이벤트는 백업 시점(100개 단위)에만 전송(실시간 데이터가 아님)
- 따라서 조회수는 조회수 서비스로 직접 요청해서 가져와본다.
  - 대신, 게시글 조회 서비스에서 짧은 만료 시간이라도 캐시해서 부하를 줄여보도록 한다.
  - 이러한 캐시는, 캐시 최적화 전략에서 더욱 개선해본다.

