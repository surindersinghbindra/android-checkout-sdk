def disable_startup(manifest_path):
    with open(manifest_path, 'r') as f:
        content = f.read()
    
    if 'tools:node="remove"' not in content:
        provider_block = """
        <provider
            android:name="androidx.startup.InitializationProvider"
            android:authorities="${applicationId}.androidx-startup"
            android:exported="false"
            tools:node="merge">
            <meta-data
                android:name="com.caribeanroyal.ecommercesample.sdk.checkout.core.db.CheckoutSdkInitializer"
                tools:node="remove" />
        </provider>
    </application>"""
        content = content.replace('</application>', provider_block)
        content = content.replace('<manifest xmlns:android="http://schemas.android.com/apk/res/android">', '<manifest xmlns:android="http://schemas.android.com/apk/res/android"\n    xmlns:tools="http://schemas.android.com/tools">')
        with open(manifest_path, 'w') as f:
            f.write(content)

disable_startup('app/src/main/AndroidManifest.xml')
disable_startup('app-headless/src/main/AndroidManifest.xml')

