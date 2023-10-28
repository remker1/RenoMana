from flask import Flask, jsonify, render_template, request, redirect, url_for
import pymongo
from pymongo import MongoClient

app = Flask(__name__)
client = MongoClient(host='db', port=27017, username='root', password='pass')
db = client.renoGp

################ Table Definitions ################


# Displays the RenoGrpRequestPage.html webpage and allows user to input data to mango_tb database table
@app.route('/', methods=['GET'])
def display():
    try:
        return render_template('RenoGrpRequestPage.html')
    except:
        pass




# Displays the data from the database table mango_tb
@app.route('/database')
def get_stored_data():
    try:
        _table = db.mango_tb.find()
        data = [{"id": person["id"], "name": person["name"], "role": person["role"]} for person in _table]
        return jsonify({"Data": data})
    except:
        pass


# @app.route('/register', methods=['POST'])
# def register():
#     try:
#         data = request.get_json()
#         print("-------------------------")
#         print("username: " + data['username'])
#         print("password: " + data['password'])
#         print("email: " + data['email'])
#         print("cellNumber: " + data['cellNumber'])
#
#         # Assuming data is a dictionary containing the fields you want to add
#         document = {
#             'username': data['username'],
#             'pass': data['password'],
#         }
#
#         print(document)
#
#         # Insert the document into the collection
#         result = db['auth'].insert_one(document)
#
#         response = {
#             'status': 'success',
#             'message': 'Document added successfully',
#             'inserted_id': str(result.inserted_id)
#         }
#
#
#         return jsonify(response), 200
#     except:
#         response = {
#             'status': 'failure',
#             'message': 'Operation failed.'
#         }
#         print("something went wrong")
#         return jsonify(response), 500

@app.route('/register', methods=['POST'])
def register():
    print("Register qwerty")
    try:
        data = request.get_json()
        print("-------------------------")
        print("fname: " + data['fname'])
        print("lname: " + data['lname'])
        print("username: " + data['username'])
        print("password: " + data['password'])
        print("email: " + data['email'])
        print("cellNumber: " + data['cellNumber'])

        # Assuming data is a dictionary containing the fields you want to add
        auth_document = {
            'username': data['username'],
            'password': data['password']
        }

        employee_document = {
            'fname': data['fname'],
            'lname': data['lname'],
            'username': data['username'],
            'email': data['email'],
            'cellNumber': data['cellNumber'],
            'projects': "",
        }

        # Insert the document into the collection
        result1 = db['auth'].insert_one(auth_document)
        result2 = db['employees'].insert_one(employee_document)



        response = {
            'status': 'success',
            'message': 'Document added successfully',
            'inserted_id1': str(result1.inserted_id),
            'inserted_id2': str(result2.inserted_id)
        }

        return jsonify(response), 200
    except Exception as e:
        # Log the exception for debugging
        print(f'Error in register route: {e}')
        return jsonify({"status": "failure", "message": str(e)}), 500

@app.route('/login', methods=['POST'])
def login():
    try:
        data = request.get_json() #{username: ____, password:______}

        input_user = data['username']
        input_pass = data['password']

        cookie = ""

        user_exists = db['auth'].find_one({"username": input_user})

        if user_exists:
            if input_pass == user_exists["password"]:
                cookie = user_exists["username"]
                response = {
                    'status': 'success',
                    'message': 'Login Successful',
                    'cookie': cookie
                }
                print(response)
                return jsonify(response), 200
            else:
                response = {
                    'status': 'failed',
                    'message': 'Password doesn\'t match',
                    'cookie': cookie
                }
                print(response)
                return jsonify(response), 500
        else:
            response = {
                'status': 'failed',
                'message': 'Username doesn\'t exist',
                'cookie': cookie
            }
            print(response)
            return jsonify(response), 500

    except Exception as e:
        # Log the exception for debugging
        print(f'Error in register route: {e}')
        return jsonify({"status": "failure", "message": str(e)}), 500


@app.route('/submitRequest', methods=['GET', 'POST'])
def submit_request():
    try:
        if request.method == 'POST':
            # Extract user inputs from the HTML form
            # data_1000 = request.get_json()
            customerName = request.form.get("customerName")
            customerEmail = request.form.get("customerEmail")
            customerCell = request.form.get("customerCell")
            company = request.form.get("company")
            startDate = request.form.get("startDate")
            endDate = request.form.get("endDate")
            projectDesc = request.form.get("projectDesc")
            projectInq = request.form.get("projectInq")

            user_document = {
                'customerName': customerName,
                'customerEmail': customerEmail,
                'customerCell': customerCell,
                'company': company,
                'startDate': startDate,
                'endDate': endDate,
                'projectDesc': projectDesc
            }

            inq_document = {
                'projectInq': projectInq
            }
            # Insert the user data into the MongoDB collection
            result100 = db['user'].insert_one(user_document)
            result200 = db['inq'].insert_one(inq_document)

            response_1000 = {
                'status': 'success',
                'message': 'Data submitted successfully',
                'inserted_id': str(result100.inserted_id)
            }
            return jsonify(response_1000), 200

    except Exception as e:
        # Log the exception for debugging
        print(f'Error in submit_request route: {e}')
        response_1000 = {
            'status': 'failure',
            'message': str(e)
        }
        return jsonify(response_1000), 500


if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True)
