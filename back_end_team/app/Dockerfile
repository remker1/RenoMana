FROM python:3.6

WORKDIR /app
RUN mkdir ./templates

# Copy files into the docker container
COPY  app.py ./
COPY requirements.txt ./
COPY ./templates/* ./templates

# Install requirements for Flask server
RUN pip install -r ./requirements.txt

# Run the flask server when the docker container is built
CMD python -u app.py
