# Kütüphane Yönetim Sistemi JUnit Testleri

Bu projede, JUnit kullanarak RaporlamaFacade sınıfının birim ve entegrasyon testlerini oluşturuyoruz.

## JUnit Nedir?

JUnit, Java projelerinde birim ve entegrasyon testleri yazmanızı sağlayan bir test framework'üdür. JUnit, test yazmayı, çalıştırmayı ve sonuçlarını değerlendirmeyi kolaylaştırır.

## Birim Test ve Entegrasyon Testleri Arasındaki Fark

1. **Birim Testler (Unit Tests)**:
   - Kodun en küçük parçalarını (genellikle bir sınıf veya metod) izole şekilde test eder
   - Harici kaynaklardan bağımsızdır (veritabanı, API, vb.)
   - Mock veya Stub nesneleri kullanılarak bağımlılıklar simüle edilir
   - Hızlı çalışır, çünkü gerçek bağımlılıkları kullanmaz

2. **Entegrasyon Testleri (Integration Tests)**:
   - Birden fazla bileşenin bir arada nasıl çalıştığını test eder
   - Gerçek bağımlılıkları kullanır (gerçek veritabanı, gerçek servisler, vb.)
   - Daha yavaş çalışır, ancak sistemin gerçek davranışını daha iyi yansıtır

## Projede JUnit Kullanımı

Bu projede, RaporlamaFacade sınıfı için iki tür test bulunmaktadır:

### 1. Birim Testleri (`RaporlamaFacadeTest.java`)

Birim testlerinde, Mockito kütüphanesi kullanılarak bağımlılıklar (servisler) simüle edilir. Bu testler, RaporlamaFacade sınıfının mantığını veritabanı erişimi olmadan test eder.

```java
@ExtendWith(MockitoExtension.class)
public class RaporlamaFacadeTest {
    @Mock
    private KitapService kitapService;
    
    @InjectMocks
    private RaporlamaFacade raporlamaFacade;
    
    // Test metodları...
}
```

### 2. Entegrasyon Testleri (`RaporlamaFacadeIntegrationTest.java`)

Entegrasyon testlerinde, Spring Boot test altyapısı kullanılarak gerçek bir uygulama başlatılır ve veritabanına gerçek erişimle testler gerçekleştirilir.

```java
@SpringBootTest
@Transactional
public class RaporlamaFacadeIntegrationTest {
    @Autowired
    private RaporlamaFacade raporlamaFacade;
    
    // Test metodları...
}
```

## Testleri Çalıştırma

Testleri çalıştırmak için Maven kullanabilirsiniz:

```bash
# Tüm testleri çalıştırma
mvn test

# Belirli bir test sınıfını çalıştırma
mvn test -Dtest=RaporlamaFacadeTest

# Belirli bir test metodunu çalıştırma
mvn test -Dtest=RaporlamaFacadeTest#testGenelDurumRaporu
```

## JUnit Ek Bilgiler

- **Annotations**:
  - `@Test`: Test metodu belirteci
  - `@BeforeEach`: Her test metodundan önce çalışacak metod
  - `@AfterEach`: Her test metodundan sonra çalışacak metod
  - `@BeforeAll`: Tüm testlerden önce bir kez çalışacak statik metod
  - `@AfterAll`: Tüm testlerden sonra bir kez çalışacak statik metod

- **Assertion Metodları**:
  - `assertEquals(expected, actual)`: İki değerin eşit olup olmadığını kontrol eder
  - `assertTrue(condition)`: Koşulun doğru olup olmadığını kontrol eder
  - `assertFalse(condition)`: Koşulun yanlış olup olmadığını kontrol eder
  - `assertNotNull(object)`: Nesnenin null olmadığını kontrol eder
  - `assertThrows(exceptionClass, executable)`: Belirtilen kodun belirtilen istisnayı fırlattığını kontrol eder

## Mockito Kullanımı

Mockito, Java'da mock nesneleri oluşturmak için kullanılan popüler bir kütüphanedir. Birim testlerde bağımlılıkları simüle etmek için kullanılır.

- `@Mock`: Bir sınıfın taklit edilmiş (mock) bir örneğini oluşturur
- `@InjectMocks`: Mock nesnelerini bir sınıfa enjekte eder
- `when(mockObject.method()).thenReturn(value)`: Bir mock metodun davranışını belirler
- `verify(mockObject).method()`: Bir metodun çağrılıp çağrılmadığını doğrular

## Yeni Testler Ekleme

Yeni testler eklemek istediğinizde, aşağıdaki adımları izleyebilirsiniz:

1. Test edilecek sınıf için yeni bir test sınıfı oluşturun (örn. `YeniSinifTest.java`)
2. Gerekli bağımlılıkları `@Mock` veya `@Autowired` ile tanımlayın
3. Test metodlarını `@Test` annotation'ı ile işaretleyerek yazın
4. Assertion'lar ile beklenen davranışı doğrulayın 