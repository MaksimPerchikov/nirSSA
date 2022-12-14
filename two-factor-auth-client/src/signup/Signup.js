import React, { useEffect, useState } from "react";
import { Form, Input, Button, notification, Checkbox } from "antd";
import { Redirect } from "react-router-dom";
import { DingtalkOutlined } from "@ant-design/icons";
import { signup } from "../util/ApiUtil";
import "./Signup.css";

const Signup = (props) => {
  const [loading, setLoading] = useState(false);
  const [redirect, setRedirect] = useState();
  const [qrImageUrl, setQrImageUrl] = useState();

  useEffect(() => {
    if (localStorage.getItem("accessToken") !== null) {
      props.history.push("/");
    }
  }, []);

  const onFinish = (values) => {
    setLoading(true);
    signup(values)
      .then((response) => {
        notification.success({
          message: "Success",
          description:
            "Thank you! You're successfully registered. Please Login to continue!",
        });
        if (response.mfa) {
          console.log(response);
          setQrImageUrl(response.secretImageUri);
          setRedirect("/qrcode");
        } else {
          setRedirect("/");
        }

        setLoading(false);
      })
      .catch((error) => {
        notification.error({
          message: "Error",
          description:
            error.message || "Sorry! Something went wrong. Please try again!",
        });
        setLoading(false);
      });
  };

  if (redirect) {
    return (
      <Redirect to={{ pathname: redirect, state: { imageUrl: qrImageUrl } }} />
    );
  }

  return (
    <div className="login-container">
      <DingtalkOutlined style={{ fontSize: 50 }} />
      <Form
        name="normal_login"
        className="login-form"
        initialValues={{ remember: true }}
        onFinish={onFinish}
      >
        <Form.Item
          name="name"
          rules={[{ required: true, message: "Введите name!" }]}
        >
          <Input size="large" placeholder="Name" />
        </Form.Item>
        <Form.Item
          name="username"
          rules={[{ required: true, message: "Введите Username!" }]}
        >
          <Input size="large" placeholder="Username" />
        </Form.Item>
        <Form.Item
          name="email"
          rules={[{ required: true, message: "Введите email!" }]}
        >
          <Input size="large" placeholder="Email" />
        </Form.Item>
        <Form.Item
          name="password"
          rules={[{ required: true, message: "Введите пароль!" }]}
        >
          <Input size="large" type="password" placeholder="Password" />
        </Form.Item>
        <Form.Item name="mfa" valuePropName="checked">
          <Checkbox>использовать two-factor authentication</Checkbox>
        </Form.Item>
        <Form.Item>
          <Button
            shape="round"
            size="large"
            htmlType="submit"
            className="login-form-button"
            loading={loading}
          >
            Регистрация
          </Button>
        </Form.Item>
        Уже зарегистрированы? <a href="/login">Авторизоваться</a>
      </Form>
    </div>
  );
};

export default Signup;
