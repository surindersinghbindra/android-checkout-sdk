package com.caribeanroyal.ecommercesample.di;

import android.content.Context;
import com.caribeanroyal.ecommercesample.core.database.AppDatabase;
import com.caribeanroyal.ecommercesample.core.database.di.DatabaseModule;
import com.caribeanroyal.ecommercesample.core.database.di.DatabaseModule_ProvideAppDatabaseFactory;
import com.caribeanroyal.ecommercesample.core.network.di.NetworkModule;
import com.caribeanroyal.ecommercesample.core.network.di.NetworkModule_ProvideRetrofitFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.InstanceFactory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import javax.annotation.processing.Generated;
import retrofit2.Retrofit;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class DaggerAppComponent {
  private DaggerAppComponent() {
  }

  public static AppComponent.Factory factory() {
    return new Factory();
  }

  private static final class Factory implements AppComponent.Factory {
    @Override
    public AppComponent create(Context context) {
      Preconditions.checkNotNull(context);
      return new AppComponentImpl(new NetworkModule(), new DatabaseModule(), context);
    }
  }

  private static final class AppComponentImpl implements AppComponent {
    private final AppComponentImpl appComponentImpl = this;

    private Provider<Retrofit> provideRetrofitProvider;

    private Provider<Context> contextProvider;

    private Provider<AppDatabase> provideAppDatabaseProvider;

    private AppComponentImpl(NetworkModule networkModuleParam, DatabaseModule databaseModuleParam,
        Context contextParam) {

      initialize(networkModuleParam, databaseModuleParam, contextParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final NetworkModule networkModuleParam,
        final DatabaseModule databaseModuleParam, final Context contextParam) {
      this.provideRetrofitProvider = DoubleCheck.provider(NetworkModule_ProvideRetrofitFactory.create(networkModuleParam));
      this.contextProvider = InstanceFactory.create(contextParam);
      this.provideAppDatabaseProvider = DoubleCheck.provider(DatabaseModule_ProvideAppDatabaseFactory.create(databaseModuleParam, contextProvider));
    }

    @Override
    public Retrofit retrofit() {
      return provideRetrofitProvider.get();
    }

    @Override
    public AppDatabase appDatabase() {
      return provideAppDatabaseProvider.get();
    }
  }
}
