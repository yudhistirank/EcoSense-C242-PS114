from google.cloud import firestore

async def store_data(id, data):
    db = firestore.Client()
    predict_ref = db.collection('predictions')
    return predict_ref.document(id).set(data)