from flask import Flask, jsonify, render_template, request
import pymongo
from pymongo import MongoClient
from bson import json_util


app = Flask(__name__)
client = MongoClient(host='db', port=27017, username='root', password='pass')
db = client.renoGp
employeeID = 1


# Displays the main Reno Group landing page
@app.route('/', methods=['GET'])
def displayLanding():
    try:
        return render_template('landing.html')
    except:
        pass


# Displays the RenoGrpRequestPage.html webpage and allows user to input data to mango_tb database table
@app.route('/requestPage', methods=['GET'])
def displayRequestPage():
    try:
        return render_template('RenoGrpRequestPage.html')
    except:
        pass


# Displays the RenoGrpReviewPage.html webpage and allows users to add and view customer reviews
@app.route('/reviews', methods=['GET'])
def displayReviews():
    try:
        return render_template('RenoGrpReviewPage.html')
    except:
        pass


@app.route('/inquiry', methods=['GET'])
def displayInquiry():
    try:
        return render_template('RenoGrpInquiryPage.html')
    except:
        pass


# Initialize some default employees into the database
@app.route('/initDB', methods=["GET"])
def initDB():
    try:
        # Insert employees into the database
        employeeList = [
            {
                "id": 1,
                "first_name": "Bob",
                "last_name": "Smith",
                "cell_number": "123-456-7890",
                "employee_type": "contractor"
            },
            {
                "id": 2,
                "first_name": "Steven",
                "last_name": "Johnson",
                "cell_number": "987-654-3210",
                "employee_type": "contractor"
            },
            {
                "id": 3,
                "first_name": "Sam",
                "last_name": "Davis",
                "cell_number": "555-555-5555",
                "employee_type": "contractor"
            }
        ]
        result = db['employees'].insert_many(employeeList)

        response = {
            'status': 'success',
            'message': 'Data submitted successfully',
            'inserted_id': str(result)
        }
        return jsonify(response), 200

    except Exception as e:
        # Log the exception for debugging
        print(f'Error in initDB route: {e}')
        response = {
            'status': 'failure',
            'message': str(e)
        }
        return jsonify(response), 500


@app.route('/register', methods=['POST'])
def register():
    global employeeID, title

    if employeeID == 1:
        title = "Manager"
    elif employeeID > 1:
        title = "Employee"

    print("Register qwerty")
    try:
        data = request.get_json()
        print("-------------------------")
        print("id: " + str(employeeID))
        print("fname: " + data['fname'])
        print("lname: " + data['lname'])
        print("username: " + data['username'])
        print("password: " + data['password'])
        print("email: " + data['email'])
        print("cellNumber: " + data['cellNumber'])
        print("title: " + title)

        # Assuming data is a dictionary containing the fields you want to add
        auth_document = {
            'username': data['username'],
            'password': data['password']
        }

        employee_document = {
            'id': employeeID,
            'fname': data['fname'],
            'lname': data['lname'],
            'username': data['username'],
            'email': data['email'],
            'cellNumber': data['cellNumber'],
            'title': title,
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
        employeeID += 1
        return jsonify(response), 200
    except Exception as e:
        # Log the exception for debugging
        print(f'Error in register route: {e}')
        return jsonify({"status": "failure", "message": str(e)}), 500


# Route for submitting login
@app.route('/login', methods=['POST'])
def login():
    try:
        data = request.get_json()  # {username: ____, password:______}
        print(data)

        input_user = data['username']
        input_pass = data['password']

        cookie = ""

        user_exists = db['auth'].find_one({"username": input_user})
        user_exists_title = db['employees'].find_one({"username": input_user})

        if user_exists:
            if input_pass == user_exists["password"]:
                cookie = { "username": user_exists["username"], "title": user_exists_title["title"] }
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


@app.route('/getDashboardData', methods=['POST'])
def getDashboardData():
    try:
        data = request.get_json()
        queryUser = data['cookie']

        cursor = db['employees'].find({"username": queryUser})
        result = [doc for doc in cursor]

        for doc in result:
            if '_id' in doc:
                doc['_id'] = str(doc['_id'])

        if result:
            return jsonify(result[0]), 200
        else:
            response = {
                "message": "Could not find the user"
            }
            return jsonify(response), 500
    except Exception as e:
        print(e)
        response = {
            "message": str(e)
        }
        return jsonify(response), 500


@app.route('/getDashboardDataTest', methods=['POST'])
def getDashboardDataTest():
    try:
        data = request.get_json()
        queryUser = data['cookie']

        print(queryUser)

        employees_cursor = db['employees'].find({"username": queryUser})
        employees_result = [str_id(doc) for doc in employees_cursor]

        projects_cursor = db['projects'].find({"members": queryUser})
        projects_result = [str_id(doc) for doc in projects_cursor]
        project_ids = [str(doc['projectID']) for doc in projects_result]

        print(project_ids)

        inventory_cursor = db['inventory'].find({"itemProject": {"$in": project_ids}})
        inventory_result = [str_id(doc) for doc in inventory_cursor]

        aggregated_result = {
            "employees": employees_result,
            "projects": projects_result,
            "inventory": inventory_result
        }

        print(aggregated_result)

        if aggregated_result:
            return jsonify(aggregated_result), 200
        else:
            response = {"message": "Could not find the data"}
            return jsonify(response), 404
    except Exception as e:
        print(e)
        response = {"message": str(e)}
        return jsonify(response), 500


def str_id(doc):
    """Convert MongoDB _id to string."""
    if '_id' in doc:
        doc['_id'] = str(doc['_id'])
    return doc


@app.route('/getProjectsData', methods=['POST'])
def getProjectsData():
    try:
        data = request.get_json()
        cookie = data['username']
        print("cookie: " + cookie)

        # Fetching the documents from MongoDB
        cursor = db['projects'].find({})

        # Converting cursor to a list and then to JSON
        response = json_util.dumps(list(cursor))
        json_data = json_util.dumps(list(cursor))
        print(response)

        # if cookie in json_data["members"]:
        #     return response, 200
        # else:
        #     return response, 400

        return response, 200
    except Exception as e:
        response = {
            'status': 'failure',
            'message': str(e)
        }
        return jsonify(response), 500


# Route for submitting requests


@app.route('/submitRequest', methods=['POST', 'GET'])
def submit_request():
    try:
        if request.method == 'GET':
            # Retrieve data from MongoDB collection
            user_data = db['projects'].find({}, {'_id': 0, 'customerName': 1, 'customerEmail': 1, 'customerCell': 1,
                                                 'company': 1, 'endDate': 1, 'projectDesc': 1})

            # Convert cursor to list for JSON serialization
            user_data_list = list(user_data)

            # Return data as JSON response
            return jsonify({"data": user_data_list})

        elif request.method == 'POST':
            customerName = request.form.get("customerName")
            customerEmail = request.form.get("customerEmail")
            customerCell = request.form.get("customerCell")
            company = request.form.get("company")
            # startDate = request.form.get("startDate")
            endDate = request.form.get("endDate")
            projectDesc = request.form.get("projectDesc")

            counter = db['counters'].find_one_and_update(
                {'_id': 'project_id'},
                {'$inc': {'seq': 1}},
                upsert=True,
                return_document=pymongo.ReturnDocument.AFTER
            )

            project_id = counter['seq'] if counter else 0
            user_document = {
                'id': project_id,
                'customerName': customerName,
                'customerEmail': customerEmail,
                'customerCell': customerCell,
                'company': company,
                # 'startDate': startDate,
                'endDate': endDate,
                'projectDesc': projectDesc,
                'members': []
            }

            db['projects'].insert_one(user_document)

            response = {
                'status': 'success',
                'message': 'Project submitted successfully',
                'projectID': project_id
            }
            return jsonify(response), 200

    except Exception as e:
        print(f'Error in submit_request route: {e}')
        response = {
            'status': 'failure',
            'message': str(e)
        }
        return jsonify(response), 500


@app.route('/submitinquiry', methods=['GET', 'POST'])
def submit_inquiry():
    try:
        if request.method == 'GET':
            # Retrieve data from MongoDB collection
            user_data = db['inq'].find({}, {'_id': 0, 'Name': 1, 'Contact': 1, 'projectInq': 1})

            # Convert cursor to list for JSON serialization
            user_data_list = list(user_data)

            # Return data as JSON response
            return jsonify({"inq": user_data_list})

        elif request.method == 'POST':
            # Extract user inputs from the HTML form
            # data_1000 = request.get_json()
            Name = request.form.get("Name")
            Contact = request.form.get("Contact")
            projectInq = request.form.get("projectInq")

            inq_document = {
                'Name': Name,
                'Contact': Contact,
                'projectInq': projectInq
            }

            # Insert the user data into the MongoDB collection
            result200 = db['inq'].insert_one(inq_document)

            response_1000 = {
                'status': 'success',
                'message': 'Data submitted successfully',
                'inserted_id': str(result200.inserted_id)
            }
            return jsonify(response_1000), 200

    except Exception as e:
        # Log the exception for debugging
        print(f'Error in submit_inquiry route: {e}')
        response_1000 = {
            'status': 'failure',
            'message': str(e)
        }
        return jsonify(response_1000), 500

@app.route('/deleteInquiry', methods=['POST'])
def deleteInquiry():
    try:
        data = request.json

        result = db['inq'].delete_one({"Name": data.get('Name'), "Contact": data.get('Contact'), "projectInq": data.get('projectInq')})

        if result:
            response = {
                "message": "Successfully deleted Inquiry"
            }
            return jsonify(response), 200
        else:
            response = {
                "message":"Could not find the Inquiry"
            }
            return jsonify(response), 500
    except Exception as e:
        print(e)
        response = {
            "message": e
        }
        return jsonify(response), 500

@app.route('/deleteproject', methods=['POST'])
def delete_project():
    try:
        data = request.get_json()
        project_id = data.get('delete_project')  # Assuming 'delete_project' is the key for the ID
        print(project_id)
        if project_id is not None:
            # Perform deletion in MongoDB based on the project_id
            result = db['projects'].delete_one({'id': int(project_id)})

            if result.deleted_count > 0:
                response = {
                    "message": "Successfully deleted the project",
                    "status": "success"
                }
                return jsonify(response), 200
            else:
                response = {
                    "message": "Could not find specified project",
                    "status": "failure"
                }
                return jsonify(response), 404
        else:
            response = {
                "message": "ID not provided in the request",
                "status": "failure"
            }
            return jsonify(response), 400
    except Exception as e:
        print(e)
        response = {
            "message": str(e),
            "status": "error"
        }
        return jsonify(response), 500


# Route for adding reviews
@app.route('/addReview', methods=['POST'])
def addReview():
    try:
        # Gather review information
        data = request.json

        # Insert the review into the MongoDB collection
        result = db['reviews'].insert_one(
            {'title': data.get("title"), 'description': data.get("description"), 'rating': data.get("rating")})

        response = {
            'status': 'success',
            'message': 'Data submitted successfully',
            'inserted_id': str(result.inserted_id)
        }
        return jsonify(response), 200

    except Exception as e:
        # Log the exception for debugging
        print(f'Error in addReview route: {e}')
        response = {
            'status': 'failure',
            'message': str(e)
        }
        return jsonify(response), 500


@app.route('/getReviews', methods=['POST'])
def getReviews():
    try:
        data = request.json

        if (data.get("rating") == "0"):
            result = list(db['reviews'].find({}, {"title": 1, "description": 1, "rating": 1, "_id": 0}))
        else:
            result = list(db['reviews'].find({"rating": data.get("rating")},
                                             {"title": 1, "description": 1, "rating": 1, "_id": 0}))
        response = {
            "reviews": result
        }
        return jsonify(response), 200

    except Exception as e:
        # Log the exception for debugging
        print(f'Error in getReviews route: {e}')
        response = {
            'status': 'failure',
            'message': str(e)
        }
        return jsonify(response), 500


@app.route('/getLoginEmployeeData', methods=['POST'])
def getLoginEmployeeData():
    try:
        data = request.get_json()
        queryUser = data['cookie']

        cursor = db['employees'].find({"username": queryUser})
        result = [doc for doc in cursor]

        for doc in result:
            if '_id' in doc:
                doc['_id'] = str(doc['_id'])

        if result:
            return jsonify(result[0]), 200
        else:
            response = {
                "message": "Could not find the user"
            }
            return jsonify(response), 500
    except Exception as e:
        print(e)
        response = {
            "message": str(e)
        }
        return jsonify(response), 500


@app.route('/getEmployeesData', methods=['POST'])
def getEmployeesData():
    try:
        data = request.get_json()

        cursor = db['employees'].find({})
        result = [doc for doc in cursor]

        for doc in result:
            if '_id' in doc:
                doc['_id'] = int(str(doc['_id']), 16)

        response = {
            "employees": result
        }
        if result:
            return jsonify(response), 200
        else:
            response = {
                "message": "Could not find the user"
            }
            return jsonify(response), 500

    except Exception as e:
        print(e)
        response = {
            "message": str(e)
        }
        return jsonify(response), 500

@app.route('/addEmployeeData', methods=['POST'])
def addEmployeeData():
    try:
        data = request.get_json()

        # Assuming data is a dictionary containing the fields you want to add
        auth_document = {
            'username': data['username'],
            'password': data['password']
        }

        employee_document = {
            'id': data['id'],
            'fname': data['fname'],
            'lname': data['lname'],
            'username': data['username'],
            'email': data['email'],
            'cellNumber': data['cellNumber'],
            'title': data['title'],
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



@app.route('/modEmployeeData', methods=['POST'])
def modEmployeeData():
    try:
        data = request.get_json()

        # Assuming data is a dictionary containing the fields you want to add

        employee_mod_document = {
            'fname': data['fname'],
            'lname': data['lname'],
            'email': data['email'],
            'cellNumber': data['cellNumber'],
            'title': data['title']
        }


        # Insert the document into the collection

        result = db['employees'].update_one({'username': data['username']}, {"$set": employee_mod_document},
                                            upsert=True)

        response = {
            'status': 'success',
            'message': 'Document modified successfully',
            'modified_count': result.modified_count,
        }

        return jsonify(response), 200
    except Exception as e:
        # Log the exception for debugging
        print(f'Error in register route: {e}')
        return jsonify({"status": "failure", "message": str(e)}), 500

@app.route('/deleteEmployeeData', methods=['POST'])
def deleteEmployeeData():
    try:
        data = request.get_json()
        queryUser = data['deleteUsername']

        result1 = db['employees'].delete_one({"username": queryUser})
        result2 = db['auth'].delete_one({"username": queryUser})

        if result1.deleted_count != 0 & result2.deleted_count != 0:
            response = {
                "message": "Successfully deleted item"
            }
            return jsonify(response), 200
        else:
            response = {
                "message": "Could not find the user"
            }
            return jsonify(response), 500
    except Exception as e:
        print(e)
        response = {
            "message": e
        }
        return jsonify(response), 500

@app.route('/getProjects', methods=['POST'])
def getProjects():
    try:
        data = request.get_json()
        queryUser = data['cookie']

        result = db['projects'].find({"username": queryUser})

        if result:
            return result, 200
        else:
            response = {
                "message": "Could not find the user"
            }
            return jsonify(response), 500
    except Exception as e:
        print(e)
        response = {
            "message": e
        }
        return jsonify(response), 500


@app.route('/syncInventoryDelete', methods=['POST'])
def syncInventoryDelete():
    try:
        data = request.get_json()
        deleteID = data['deleteItemID']
        print(deleteID)

        result = db['inventory'].delete_one({"itemID": "\"" + deleteID + "\""})
        print(result)

        if result:
            response = {
                "message": "Successfully deleted item"
            }
            return response, 200
        else:
            response = {
                "message": "Could not find the item to delete"
            }
            return jsonify(response), 500
    except Exception as e:
        print(e)


@app.route('/syncInventoryAdd', methods=['POST'])
def syncInventoryAdd():
    try:
        data = request.get_json()
        result = db['inventory'].insert_many(data['items'])
        print(result)

        if result:
            response = {
                "message": "Success!"
            }
            return response, 200
        else:
            response = {
                "message": "Could not add item(s) to inventory"
            }
            return jsonify(response, 500)

    except Exception as e:
        print(e)


@app.route('/getInventoryDataInitial', methods=['GET'])
def getInventoryDataInitial():
    try:
        result_cursor = db['inventory'].find({}, {'_id': 0})
        result = list(result_cursor)
        response = {
            "items": result
        }
        return jsonify(response), 200

    except Exception as e:
        # Log the exception for debugging
        print(f'Error in getReviews route: {e}')
        response = {
            'status': 'failure',
            'message': str(e)
        }
        return jsonify(response), 500


if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5001, debug=True)
