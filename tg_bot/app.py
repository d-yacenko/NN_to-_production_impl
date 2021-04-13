# https://core.telegram.org/bots#
# https://pytorch.org/get-started/locally/
# pip3 install opencv-python
# pip3 install PyTelegramBotAPI==2.2.3


import telebot
import traceback
import torch
from torchvision import transforms
import config
from handler import *

bot = telebot.TeleBot(config.TOKEN)
classes=['fire','nofire']
model = torch.jit.load('fire_net.pt')
transform = transforms.Compose(
    [transforms.Resize(224),
     transforms.ToTensor(),
     transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])])


def get_photo(message):
    photo = message.photo[1].file_id
    file_info = bot.get_file(photo)
    file_content = bot.download_file(file_info.file_path)
    return file_content

@bot.message_handler(commands=['start'])
def start_message(message):
    bot.send_message(message.chat.id, 'Привет! Пришли фото сюда, а нейронная сеть определит наличие объекта.\nАвтор: @d-yacenko')

@bot.message_handler(content_types=['photo'])
def repeat_all_messages(message):
    try:
        file_content = get_photo(message)
        image = byte2image(file_content)
        image=transform(image)
        model.eval()
        image=torch.unsqueeze(image, 0)
        outputs = model(image)
        _, preds = torch.max(outputs, 1)
        bot.send_message(message.chat.id,text='Обнаружено: {}'.format(classes[int(preds)]))
    except Exception:
        traceback.print_exc()
        bot.send_message(message.chat.id, 'Упс, что-то пошло не так :( Обратитесь в службу поддержки!')

if __name__ == '__main__':
    import time
    while True:
        try:
            bot.polling(none_stop=True)
        except Exception as e:
            time.sleep(15)
            print('Restart!')
