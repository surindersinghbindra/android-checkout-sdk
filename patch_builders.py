with open('app/src/main/java/com/caribeanroyal/ecommercesample/MainActivity.kt', 'r') as f:
    content = f.read()

content = content.replace('CheckoutSdk.Builder()', 'CheckoutSdk.Builder(applicationContext)')

with open('app/src/main/java/com/caribeanroyal/ecommercesample/MainActivity.kt', 'w') as f:
    f.write(content)

with open('app-headless/src/main/java/com/caribeanroyal/headless/MainActivity.kt', 'r') as f:
    content2 = f.read()

content2 = content2.replace('CheckoutSdk.Builder()', 'CheckoutSdk.Builder(applicationContext)')

with open('app-headless/src/main/java/com/caribeanroyal/headless/MainActivity.kt', 'w') as f:
    f.write(content2)
