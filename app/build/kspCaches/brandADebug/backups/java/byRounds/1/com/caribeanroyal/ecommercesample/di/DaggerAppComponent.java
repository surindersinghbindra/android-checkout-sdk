package com.caribeanroyal.ecommercesample.di;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Preconditions;
import javax.annotation.processing.Generated;

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
      return new AppComponentImpl(context);
    }
  }

  private static final class AppComponentImpl implements AppComponent {
    private final AppComponentImpl appComponentImpl = this;

    private AppComponentImpl(Context contextParam) {


    }
  }
}
