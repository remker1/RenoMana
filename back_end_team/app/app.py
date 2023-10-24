from flask import Flask, jsonify, render_template, request, redirect, url_for
import pymongo
from pymongo import MongoClient

app = Flask(__name__)

# Gets the mongodb database instance
client = MongoClient(host='localhost',
                     port=27017,
                     username='root',
                     password='pass',
                     authSource="admin")
db = client["renoGrp"]



################ Table Definitions ################


# Displays the index.html webpage and allows user to input data to mango_tb database table
@app.route('/', methods=['GET'])
def display():
    try:
        return render_template('index.html')
    except:
        pass
    finally:
        if type(db) == MongoClient:
            db.close()


# Displays the data from the database table mango_tb
@app.route('/database')
def get_stored_data():
    try:
        _table = db.mango_tb.find()
        data = [{"id": person["id"], "name": person["name"], "role": person["role"]} for person in _table]
        return jsonify({"Data": data})
    except:
        pass
    finally:
        if type(db) == MongoClient:
            db.close()


@app.route('/register', methods=['POST'])
def register():
    try:
        data = request.get_json()
        print("-------------------------")
        print("username: " + data['username'])
        print("password: " + data['password'])
        print("email: " + data['email'])
        print("cellNumber: " + data['cellNumber'])

        response = {
            'status': 'success',
            'message': 'Operation successful.'
        }


        return jsonify(response), 200
    except:
        response = {
            'status': 'failure',
            'message': 'Operation failed.'
        }
        print("something went wrong")
        return jsonify(response), 500


if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True)
