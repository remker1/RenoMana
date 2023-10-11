from flask import Flask, render_template, request, redirect, url_for
import sqlite3

app = Flask(__name__)

# Define the function to insert user prompts into the database
def insert_user_prompt(gender, name):
    conn = sqlite3.connect('database.sqlite')
    cursor = conn.cursor()
    cursor.execute('INSERT INTO user_prompts (gender, name) VALUES (?, ?)', (gender, name))
    conn.commit()
    conn.close()

@app.route('/')
def home():
    return render_template('index.html')


@app.route('/submit', methods=['POST'])
def submit():
    if request.method == 'POST':
        gender = request.form['gender']
        name = request.form['name']
        insert_user_prompt(gender, name)
        return redirect(url_for('thank_you'))  # Redirect to the thank_you route

@app.route('/thank_you')
def thank_you():
    return "Thank you for your submission!"

@app.route('/view_prompts')
def view_prompts():
    conn = sqlite3.connect('database.sqlite')
    cursor = conn.cursor()
    cursor.execute('SELECT * FROM user_prompts')
    prompts = cursor.fetchall()
    conn.close()
    return render_template('view_prompts.html', prompts=prompts)


if __name__ == '__main__':
    app.run(debug=True)
