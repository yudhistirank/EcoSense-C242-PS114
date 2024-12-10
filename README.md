# EcoSense-C242-PS114: Back-End Services
![GitHub repo size](https://img.shields.io/github/repo-size/yudhistirank/EcoSense-C242-PS114?color=red&label=repository%20size)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/yudhistirank/EcoSense-C242-PS114?color=red)
![GitHub language count](https://img.shields.io/github/languages/count/yudhistirank/EcoSense-C242-PS114)
![GitHub top language](https://img.shields.io/github/languages/top/yudhistirank/EcoSense-C242-PS114)
![GitHub](https://img.shields.io/github/license/yudhistirank/EcoSense-C242-PS114?color=yellow)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/yudhistirank/EcoSense-C242-PS114?color=brightgreen&label=commits)

## 💻 Built with

- Python
- JavaScript
- Flask
- Hapi.js
- Visual Studio Code

## 📌 Prerequisites

Before you get started, follow these requirements

- Python
- Node.js
- JavaScript
- Firestore Document
- Cloud Storage Bucket
- nanoid^3.0.0

## 🍃 How to Setup

1. Download or Clone the repository by running the following command in your terminal or Git Bash:
  ```bash
  git clone -b CC https://github.com/yudhistirank/EcoSense-C242-PS114.git
  ```
2. Move the project to the selected directory
   - Articles: `articles/`
   - Model Predict: `ecosense-backend/`
3. Open the project with a code editor
   - Recommended editors: Visual Studio Code
4. Install dependencies by running the following command in your terminal:
   - Articles: `npm i`
   - Model Predict: `pip install -r requirements.txt`
5. Configure Cloud Storage Bucket, Firestore, and Service Account to access the firestore
6. Add Firebase configuration
   - Articles: `articles/src/handler.js`
   - Model Predict: `ecosense-backend/app/services/get_history.py` and `ecosense-backend/app/services/store_data.py`
7. Add Cloud Storage configuration
   - Articles: `articles/src/imgServices.js`
   - Model Predict: `ecosense-backend/app/.env`
8. **Important**: Please use the nanoid version as specified.


## 🚀 How to Run

- Articles: `npm run start`
- Model Predict `flask run`
  
## 💎 Dependencies

- Flask - https://pypi.org/project/Flask/
- tensorflow-cpu - https://www.tensorflow.org/
- Werkzeug - https://pypi.org/project/Werkzeug/
- flask-cors - https://pypi.org/project/Flask-Cors/
- python-dotenv - https://pypi.org/project/python-dotenv/
- numpy - https://pypi.org/project/numpy/
- Pillow - https://pypi.org/project/pillow/
- asyncio - https://pypi.org/project/asyncio/
- asgiref - https://pypi.org/project/asgiref/
- gunicorn - https://pypi.org/project/gunicorn/
- opencv-python - https://pypi.org/project/opencv-python/
- google-cloud-firestore - https://pypi.org/project/google-cloud-firestore/
- requests - https://pypi.org/project/requests/
- @google-cloud/firestore - https://www.npmjs.com/package/@google-cloud/firestore
- @google-cloud/storage - https://www.npmjs.com/package/@google-cloud/storage
- @hapi/hapi - https://www.npmjs.com/package/@hapi/hapi
- @hapi/inert - https://www.npmjs.com/package/@hapi/inert
- dateformat - https://www.npmjs.com/package/dateformat
- nanoid - https://www.npmjs.com/package/nanoid

## 🖼️ API Documentation

### Base URL

- Model Predict: https://waste-classification-ecosense-37260345559.asia-southeast2.run.app
- Articles: https://articles-37260345559.asia-southeast2.run.app

### Endpoint
- Model Predict:
  - `/predict`
  - `/predict/histories`
- Articles:
  - `/articles`
  - `/articles/{article id}`
  - `/articles?category=anorganik`
  - `/articles?category=organik`

### Documentation

[Click here to view](https://docs.google.com/document/d/1pBK7wrGKkahSSEeih_oSveyixqN75wD5iRYUxa9RTf8/edit?usp=sharing)

## 👨‍💻 Developed By

EcoSense Capstone Team <br>
C242-PS114 <br>
Bangkit Academy

## 💬 Contact

If you want to contact us, leave a message via email

Email - [EcoSense Team](mailto:C242-PS114@bangkit.academy)

## ❤️ Thanks

Thank you to all parties who have contributed to the development of this project

