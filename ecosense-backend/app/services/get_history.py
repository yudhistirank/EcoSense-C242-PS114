from google.cloud import firestore

async def get_history():
   db = firestore.Client(database='ml-predicts')
   predict_ref = db.collection('predictions')
   docs = predict_ref.stream()
   return [doc.to_dict() for doc in docs]