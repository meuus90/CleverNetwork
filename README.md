# CleverPlayer
 <img src="/images/icon.png" width="400px" height="400px"></img><br/>
 
 
## 목차
- [컨텐츠 소개](#컨텐츠-소개)
    - [1. 설명](#1-설명)
    - [2. UML](#2-UML)
    - [3. 결과물](#3-결과물)
- [사용한 기술](#사용한-기술)
    - [1. 다이어그램](#1-다이어그램)
    - [2. 코드 리뷰](#2-코드-리뷰)
    - [3. 소프트웨어 아키텍처 디자인 패턴](#3-소프트웨어-아키텍처-디자인-패턴)
    - [4. Firebase](#4-Firebase)
    - [5. 사용한 라이브러리](#5-사용한-라이브러리)
- [Changelog](#changelog)
- [License](#license)


## 컨텐츠 소개

### 1. 설명
Youtube 기반 음악 스트리밍 앱. Firebase Google authentication을 바탕으로 로그인 기능 구현하였다. Youtube API 를 이용하여 플레이리스트 관리, 음악 재생, 내 플레이리스트 생성을 할 수 있다. Media 타입 Notification 기능을 구현하였고 백그라운드 재생 기능을 지원한다. 저장된 플레이리스트는 편의에 따라 변경이 가능하며 개인 취향에 맞게 앱 설정을 변경할 수 있다.

### 2. UML
<img src="/images/diagram.png" width="960px" height="630px"></img><br/>
 
### 3. 결과물
<img src="/images/login.jpg" width="270px" height="555px"> </img><img src="/images/google_auth.jpg" width="270px" height="555px"> </img>
<img src="/images/main_home.jpg" width="270px" height="555px"></img><br/>

<img src="/images/main_playlist.jpg" width="270px" height="555px"> </img><img src="/images/main_setting.jpg" width="270px" height="555px"> </img>
<img src="/images/playlist.jpg" width="270px" height="555px"></img><br/>

<img src="/images/music_player.jpg" width="270px" height="555px"> </img><img src="/images/notification.jpg" width="270px" height="555px"></img><br/>




## 사용한 기술

### 1. 다이어그램

#### 데이터 플로우

<img src="/images/mvi_dataflow.png" width="757px" height="343px"></img><br/>


#### 서비스

<img src="/images/service_dataflow.png" width="683px" height="277px"></img><br/>


### 2. 코드 리뷰

#### 베이스 모듈

아키텍쳐 구성을 위한 베이스 모듈을 라이브러리로 배포하고 사용하였다. 

<https://github.com/meuus90/CleverBase> [![](https://jitpack.io/v/meuus90/CleverBase.svg)](https://jitpack.io/#meuus90/CleverBase)


#### Dependency Injection (Dagger)

UI, Viewmodel, Model 뿐만 아니라 네트워크 모듈이나 Firebase 모듈도 파편화하여 Inject 할 수 있다. Singleton 어노테이션을 사용하여 싱글턴 패턴으로 사용할 수 있다.

```
    @Singleton
    @Provides
    fun provideGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(AppConfig.clientId)
            .requestEmail()
            .build()
    }
```


#### 내외부 저장소를 연결해주는 Repository [MusicRepository](app/src/main/java/com/network/clever/data/repository/item/MusicRepository.kt)

네트워크 API 설정부분, Room 저장부분, Cache(Room) 데이터 호출부분, 네트워크 에러 시 처리, Fetch 에러 시 처리 등의 기능이 있다.
```
@Singleton
class MusicRepository
@Inject
constructor() : BaseRepository<Query>() {
    override suspend fun work(liveData: MutableLiveData<Query>): SingleLiveEvent<Resource> {
        return object :
            NetworkBoundResource<MusicListModel, MusicListModel>(liveData.value?.boundType!!) {
            override suspend fun doNetworkJob() =
                youtubeAPI.getMusics(
                    liveData.value?.params?.get(0) as String,
                    "snippet",
                    AppConfig.apiKey
                )

            override fun onNetworkError(errorMessage: String?, errorCode: Int) =
                Timber.e("Network-Error: $errorMessage")

            override fun onFetchFailed(failedMessage: String?) =
                Timber.e("Fetch-Failed: $failedMessage")
        }.getAsSingleLiveEvent()
    }
}
```


#### 저장소와 비즈니스 로직을 담당하는 UseCase [MusicUseCase](app/src/main/java/com/network/clever/domain/usecase/item/MusicUseCase.kt)

데이터 수집, 연산, 데이터 정렬 등의 비즈니스 로직을 담당한다.
```
@Singleton
class MusicUseCase
@Inject
constructor(private val repository: MusicRepository) : BaseUseCase<Params, Resource>() {
    private val liveData by lazy { MutableLiveData<Query>() }

    override suspend fun execute(
        viewModelScope: CoroutineScope,
        params: Params
    ): SingleLiveEvent<Resource> {
        setQuery(params)

        return repository.work(this@MusicUseCase.liveData)
    }

    private fun setQuery(params: Params) {
        liveData.value = params.query
    }
}
```


### 3. 소프트웨어 아키텍처 디자인 패턴

MVVM 기반의 MVI 패턴을 적용하였다. MVI는 Model-View-Intent의 약자다. MVI는 Cycle.js프레임워크의 단방향성과 Cycle Nature에서 영감을 받은 안드로이드를 위한 최신 아키텍처 패턴 중 하나이다. Model-View-Intent는 유지 관리가 용이하고 확장 가능한 앱을 만들수 있도록 도와준다.

<img src="/images/mvi.png" width="350px" height="240px"></img><br/>

주요 장점은 다음과 같다.
* 데이터가 단방향으로 순환
* View의 생명주기 동안 일관성 있는 상태를 가짐
* 불변 Model은 큰 앱에서 멀티 스레드 안정성과 안정적인 동작을 제공


### 4. Firebase

Firebase Authentication을 이용한 OAuth 인증 로그인 기능을 추가하였다.

OAuth의 개념은 다음가 같다.
* 인증을 위한 오픈 스탠다드 프로토콜 + 인증과 허가의 뜻을 모두 가짐
* 다른 어플리케이션에 아이디, 비밀번호를 노출하지않고, API 접근 권한 위임 요청 시 사용하는 방법

Firebase의 Realtime Database를 적용하였다. 해당 기능은 클라이언트 입장에서 사용방법이 Restful API와 비슷하여 쉽게 사용할 수 있다.


### 5. 사용한 라이브러리

Androidx Room 2.2.3

Androidx Livedata 2.2.0

Youtube player <https://github.com/PierfrancescoSoffritti/android-youtube-player>


## License

Completely free (MIT)! See [LICENSE.md](LICENSE.md) for more.
