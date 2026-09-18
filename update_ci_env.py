with open('.github/workflows/ci.yml', 'r') as f:
    content = f.read()

import re
content = re.sub(
    r'    steps:',
    '    env:\n      GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}\n    steps:',
    content
)

with open('.github/workflows/ci.yml', 'w') as f:
    f.write(content)
