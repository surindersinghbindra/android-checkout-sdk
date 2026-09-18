with open('app/src/main/java/com/caribeanroyal/ecommercesample/navigation/ECommerceApp.kt', 'r') as f:
    content = f.read()

content = content.replace(
    '    viewModelStoreOwner: ViewModelStoreOwner,\n    searchCruisesUseCase: SearchCruisesUseCase,',
    '    viewModelStoreOwner: ViewModelStoreOwner,'
)
content = content.replace(
    '        bookingGraph(\n            navController = navController,\n            viewModelStoreOwner = viewModelStoreOwner,\n            searchCruisesUseCase = searchCruisesUseCase\n        )',
    '        bookingGraph(\n            navController = navController,\n            viewModelStoreOwner = viewModelStoreOwner\n        )'
)
content = content.replace(
    'import com.caribeanroyal.ecommercesample.core.domain.usecase.SearchCruisesUseCase\n',
    ''
)

with open('app/src/main/java/com/caribeanroyal/ecommercesample/navigation/ECommerceApp.kt', 'w') as f:
    f.write(content)
