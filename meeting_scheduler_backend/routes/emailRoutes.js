const express = require('express');
const axios = require('axios');
const router = express.Router();

router.post('/send-email', async (req, res) => {
    const { email, title, agenda, meeting_date, start_time, end_time, location } = req.body;

    const formData = new URLSearchParams();
    formData.append('service_id', 'service_ao9lzzk');
    formData.append('template_id', 'template_xovxuko');
    formData.append('user_id', 'h-VQgL9Yx3ksmrZ6O');
    formData.append('accessToken', 'E9dtak2r9Q-MKru6AIx3S');
    formData.append('email', email);
    formData.append("title", title);
    formData.append("agenda", agenda);
    formData.append("meeting_date", meeting_date);
    formData.append("start_time", start_time);
    formData.append("end_time", end_time);
    formData.append("location", location);

    try {
        const response = await axios.post('https://api.emailjs.com/api/v1.0/email/send-form', formData, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        });
        res.status(200).send('Send Success');
    } catch (error) {
        console.error(error);
        res.status(500).send('Error sending email');
    }
});

module.exports = router;
