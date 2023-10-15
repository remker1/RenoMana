from flask import Flask, jsonify, render_template, request, redirect, url_for
import pymongo
from pymongo import MongoClient

app = Flask(__name__)

# Gets the mongodb database instance
def get_db():
    client = MongoClient(host='mongodb',
                        port=27017, 
                        username='root', 
                        password='pass',
                        authSource="admin")
    db = client["mango_db"]
    return db


# Displays the index.html webpage and allows user to input data to mango_tb database table
@app.route('/', methods=['GET', 'POST'])
def add_person():
    db=""
    try:
        db = get_db()
        if request.method == 'POST':
            
            ID = request.form['id']
            name = request.form['name']
            role = request.form['role']

            db.mango_tb.insert_one({'id': ID, 'name': name, 'role': role})
            return redirect(url_for('add_person'))

        all_people = db.mango_tb.find()
        return render_template('index.html', people=all_people)
    
    except:
        pass
    finally:
        if type(db)==MongoClient:
            db.close()

        
# Displays the data from the database table mango_tb
@app.route('/database')
def get_stored_data():
    db=""
    try:
        db = get_db()
        _table = db.mango_tb.find()
        data = [{"id": person["id"], "name": person["name"], "role": person["role"]} for person in _table]
        return jsonify({"Data": data})
    except:
        pass
    finally:
        if type(db)==MongoClient:
            db.close()

if __name__=='__main__':
    app.run(host="0.0.0.0", port=5000)