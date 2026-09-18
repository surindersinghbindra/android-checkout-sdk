with open('/Users/surindersingh/.gemini/antigravity/brain/a0a3a4ec-731d-4d49-9b4e-919ae18f772d/interview_prep.md', 'r') as f:
    prep_content = f.read()

# Remove the title and description
start_idx = prep_content.find('## 1.')
if start_idx != -1:
    prep_content = prep_content[start_idx:]
    
# Remove the Interview Talking Points section
end_idx = prep_content.find('## 6. Interview Talking Points')
if end_idx != -1:
    prep_content = prep_content[:end_idx]

# Change pronouns
prep_content = prep_content.replace('You built', 'We built')
prep_content = prep_content.replace('You heavily leveraged', 'We heavily leveraged')
prep_content = prep_content.replace('You implemented', 'We implemented')

# Append to README
with open('README.md', 'a') as f:
    f.write('\n\n## 🏗️ E-Commerce SDK & App Architecture\n\n')
    f.write(prep_content)

