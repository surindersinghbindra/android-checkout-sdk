with open('app/src/main/java/com/caribeanroyal/ecommercesample/MainActivity.kt', 'r') as f:
    content = f.read()

content = content.replace(
    '        val appComponent = DaggerAppComponent.factory().create(applicationContext)\n        val searchCruisesUseCase = appComponent.searchCruisesUseCase()\n        \n        // Analytics tracker implementation',
    '        // Analytics tracker implementation'
)
content = content.replace(
    '                            viewModelStoreOwner = this@MainActivity,\n                            searchCruisesUseCase = searchCruisesUseCase\n                        )',
    '                            viewModelStoreOwner = this@MainActivity\n                        )'
)
content = content.replace(
    'import com.caribeanroyal.ecommercesample.di.DaggerAppComponent\n',
    ''
)

with open('app/src/main/java/com/caribeanroyal/ecommercesample/MainActivity.kt', 'w') as f:
    f.write(content)
