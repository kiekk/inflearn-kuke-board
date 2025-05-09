### 조회수 어뷰징 방지 정책 설계

### 어뷰징
- 게시글을 조회하면 조회수가 올라가는데,
- 어뷰저는 특정 게시글을 여러 번 조회해서 데이터를 조작할 수 있다.

- 어뷰징을 방지하기 위해서는 로그인 여부에 따라 다르게 처리할 수 있다.
  - 로그인 사용자
    - 사용자 별로 식별할 수 있다.
  - 비로그인 사용자
    - 사용자 별로 식별하기 위해 IP, User-Agent, 쿠키, 토킅 등 다양한 방법으로 식별할 수 있다.

- 강의에서는 로그인 사용자에 대해서만 식별하도록 한다.

### 어뷰징 방지 정책 설계
- 각 사용자는 게시글 1개당 10분에 1번 조회수 증가
  - 10분 동안 100번 조회하더라도 조회수는 1회만 집계된다.
  - 10분에 2회 이상 조히하는 것은 어뷰징으로 판단하고 집계하지 않는다.

### 어뷰징 방지 정책 구현
- MySQL을 사용할 경우
  - 자동 삭제를 지원하지 않기 때문에 이와 같은 기능을 직접 개발해야 한다.
  - 동시성 문제가 발생할 수 있기 때문에 이와 같은 처리도 직접 개발해야 한다.
- Redis는 Single Thread이며, TTL(Time To Live) 기능을 지원하기 때문에 직접 개발할 필요 없이 쉽게 구현이 가능하다.
- 게시글 조회는 사용자 단위로 식별하므로 key는 (articleId + userId)가 된다.
- setIfAbsent을 사용하여, key가 존재하지 않을 경우에만 조회수를 증가시킨다.
  - true: 성공
  - false: 실패
