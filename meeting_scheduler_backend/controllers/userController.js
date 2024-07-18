const db = require('../models');
const user = require('../models/user');
const User = db.User;

exports.checkLogin = async (req, res) => {
  try {
    const { email, password } = req.body;

    let user = await User.findOne({
      where: {
          email: email,
          password: password
      }
    });

    if(user) {
      res.json(user);
    }
    else {
      return res.status(404).json({ error: 'Login failed' });
    }

  } catch (error) {
    console.log("@@" + error);
    res.status(500).json({ error: 'Login failed' });
  }
};

exports.signUp = async (req, res) => {
  try {
    const user = await User.create(req.body);

    if(user) {
      res.json(user);
    }
    else {
      return res.status(404).json({ error: 'Signup failed' });
    }

  } catch (error) {
    res.status(500).json({ error: 'Signup failed' });
  }
};

exports.getAllUsers = async (req, res) => {
  const users = await User.findAll();
  res.json(users);
};

exports.createUser = async (req, res) => {
  const user = await User.create(req.body);
  res.json(user);
};

exports.getUserById = async (req, res) => {
  const user = await User.findByPk(req.params.id);
  res.json(user);
};

exports.updateUser = async (req, res) => {
  const user = await User.findByPk(req.params.id);
  await user.update(req.body);
  res.json(user);
};

exports.deleteUser = async (req, res) => {
  const user = await User.findByPk(req.params.id);
  await user.destroy();
  res.json({ message: 'User deleted' });
};
