# Use the official lightweight Python image.
FROM python:3.9-slim

# PIP Update
RUN pip install --upgrade pip

# Allow statements and log messages to immediately appear in the Cloud Run logs
ENV PYTHONUNBUFFERED=1

# Create and change to the app directory.
WORKDIR /usr/src/app

# Copying this separately prevents re-running pip install on every code change.
COPY requirements.txt ./

# Install dependencies.
RUN pip install -r requirements.txt

# Copy local code to the container image.
COPY . ./

# Run the web service on container startup.
CMD ["gunicorn", "--bind", ":8080", "--workers", "1", "--threads", "8", "--timeout", "0", "run:app"]