import numpy as np
from PIL import Image
import io
from ..exceptions.input_error import InputError
import cv2

async def predict_classification(interpreter, image_bytes):
   try:
       # Convert image bytes to OpenCV format
       nparr = np.frombuffer(image_bytes, np.uint8)
       image = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

       # Preprocess image
       image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
       image = cv2.resize(image, (150, 150))
       image = image.astype(np.float32)
       image = np.expand_dims(image, axis=0)

       # Set input tensor
       input_details = interpreter.get_input_details()
       interpreter.set_tensor(input_details[0]['index'], image)

       # Run inference
       interpreter.invoke()

       # Get prediction
       output_details = interpreter.get_output_details()
       prediction = interpreter.get_tensor(output_details[0]['index'])[0][0]

       # Klasifikasi berdasarkan threshold 0.5
       if prediction > 0.5:
           label = 'Anorganik'
           suggestion = "Sampah ini harus didaur ulang dengan benar."
       else:
           label = 'Organik' 
           suggestion = "Sampah ini bisa dikompos dan diolah menjadi pupuk!"

       return {
           'label': label,
           'confidence': float(prediction) * 100,
           'suggestion': suggestion
       }

   except Exception as error:
       raise InputError('Terjadi kesalahan dalam melakukan prediksi')