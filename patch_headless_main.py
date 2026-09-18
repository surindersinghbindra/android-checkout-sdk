with open('app-headless/src/main/java/com/caribeanroyal/headless/MainActivity.kt', 'r') as f:
    content = f.read()

import re

# Remove OkHttp and Retrofit imports
content = re.sub(r'import okhttp3.*?\n', '', content)
content = re.sub(r'import retrofit2.*?\n', '', content)
content = re.sub(r'import com.caribeanroyal.ecommercesample.core.network.repository.NetworkCruiseRepositoryImpl\n', '', content)
content = re.sub(r'import com.caribeanroyal.ecommercesample.core.network.api.CruiseApiService\n', '', content)
content = re.sub(r'import com.caribeanroyal.ecommercesample.core.network.interceptor.MockCruiseInterceptor\n', '', content)

# Add Dagger import
content = content.replace(
    'import com.caribeanroyal.ecommercesample.sdk.checkout.core.PaymentProcessorType',
    'import com.caribeanroyal.ecommercesample.sdk.checkout.core.PaymentProcessorType\nimport com.caribeanroyal.headless.di.DaggerAppComponent'
)

# Replace the manual instantiation
old_manual = """val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(MockCruiseInterceptor())
            .build()
            
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.royalcaribbean.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            
        val apiService = retrofit.create(CruiseApiService::class.java)
        val repository = NetworkCruiseRepositoryImpl(apiService)
        val searchCruisesUseCase = SearchCruisesUseCase(repository)
        val bookingFactory = BookingViewModelFactory(searchCruisesUseCase)"""

new_dagger = """val appComponent = DaggerAppComponent.factory().create(applicationContext)
        val searchCruisesUseCase = appComponent.searchCruisesUseCase()
        val bookingFactory = BookingViewModelFactory(searchCruisesUseCase)"""

content = content.replace(old_manual, new_dagger)

with open('app-headless/src/main/java/com/caribeanroyal/headless/MainActivity.kt', 'w') as f:
    f.write(content)
