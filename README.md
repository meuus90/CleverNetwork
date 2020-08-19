# CleverPlayer
 <img src="/images/icon.png" width="400px" height="400px"></img><br/>
 
 
## 목차
- [컨텐츠 소개](#컨텐츠-소개)
    - [설명](#설명)
    - [UML](#UML)
    - [결과물](#결과물)
- [사용한 기술](#사용한-기술)
    - [코드 리뷰](#코드-리뷰)
    - [소프트웨어 아키텍처 디자인 패턴](#소프트웨어-아키텍처-디자인-패턴)
    - [Kotlin](#Kotlin)
    - [Livedata](#Livedata)
    - [Room](#Room)
    - [코루틴과 쓰레드](#코루틴과-쓰레드)
    - [Firebase](#Firebase)
    - [사용한 라이브러리](#사용한-라이브러리)
- [Changelog](#changelog)
- [License](#license)

## 컨텐츠 소개

### 설명
Youtube 기반 음악 스트리밍 앱. Firebase Google authentication을 바탕으로 로그인 기능 구현하였다. Youtube API 를 이용하여 플레이리스트 관리, 음악 재생, 내 플레이리스트 생성을 할 수 있다. Media 타입 Notification 기능을 구현하였고 백그라운드 재생 기능을 지원한다. 저장된 플레이리스트는 편의에 따라 변경이 가능하며 개인 취향에 맞게 앱 설정을 변경할 수 있다.

### UML
<img src="/images/diagram.png" width="960px" height="630px"></img><br/>
 
### 결과물
<img src="/images/login.jpg" width="270px" height="555px"> </img><img src="/images/google_auth.jpg" width="270px" height="555px"> </img>
<img src="/images/main_home.jpg" width="270px" height="555px"></img><br/>

<img src="/images/main_playlist.jpg" width="270px" height="555px"> </img><img src="/images/main_setting.jpg" width="270px" height="555px"> </img>
<img src="/images/playlist.jpg" width="270px" height="555px"></img><br/>

<img src="/images/music_player.jpg" width="270px" height="555px"> </img><img src="/images/notification.jpg" width="270px" height="555px"></img><br/>




## 사용한 기술

### 코드 리뷰

#### 베이스 모듈

아키텍쳐 구성을 위한 베이스 모듈을 라이브러리로 배포하고 사용하였다. 

<https://github.com/meuus90/CleverBase> [![](https://jitpack.io/v/meuus90/CleverBase.svg)](https://jitpack.io/#meuus90/CleverBase)

#### Dependency Injection (Dagger)

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
UI, Viewmodel, Model 뿐만 아니라 네트워크 모듈이나 Firebase 모듈도 파편화하여 Inject 할 수 있다. Singleton 어노테이션을 사용하여 싱글턴 패턴으로 사용할 수 있다.


#### 내외부 저장소를 연결해주는 Repository

[MusicRepository](app/src/main/java/com/network/clever/data/repository/item/MusicRepository.kt)
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
네트워크 API 설정부분, Room 저장부분, Cache(Room) 데이터 호출부분, 네트워크 에러, Fetch 에러 등의 기능이 있다.


#### 저장소와 비즈니스 로직을 담당하는 UseCase

[MusicUseCase](app/src/main/java/com/network/clever/domain/usecase/item/MusicUseCase.kt)
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
데이터 수집, 연산, 데이터 정렬 등의 비즈니스 로직을 담당한다.


### 소프트웨어 아키텍처 디자인 패턴
### Kotlin
### Livedata
### Room
### 코루틴과 쓰레드
### Firebase
### 사용한 라이브러리


## License

Completely free (MIT)! See [LICENSE.md](LICENSE.md) for more.
