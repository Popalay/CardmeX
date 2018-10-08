import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
admin.initializeApp(functions.config().firebase);

exports.sendPushWhenAddCardRequest = functions.firestore
    .document('request/{requestId}')
    .onCreate((snap, context) => {

      const request = snap.data();

      const notification = {
        type: "ADD_CARD",
        title: "Title",
        description: "Description",
        card: request.card
    };

      // save notification
      console.log('Write new notification');
      return admin.firestore().collection('notification').add(notification).then(() => {
        console.log('Write new notification succeeded!');
      });
    });