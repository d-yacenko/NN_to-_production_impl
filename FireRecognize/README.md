Для ResNet 18


model_ft.eval()

model_ft=model_ft.to(torch.device("cpu"))

#model_ft=torch.quantization.convert(model_ft)

#torch.jit.script(model_ft).save("/content/drive/My Drive/AI/Samsung/fire_net.pt")

input_tensor = torch.rand(1,3,224,224)

script_model = torch.jit.trace(model_ft,input_tensor)

script_model.save("/content/drive/My Drive/AI/Samsung/fire_net.pt")



взято откуда то отсюда:


https://heartbeat.fritz.ai/pytorch-mobile-image-classification-on-android-5c0cfb774c5b
