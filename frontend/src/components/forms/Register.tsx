import React, { useState } from 'react';
import { useAuth } from '../../api/AuthContext.tsx';
import {type RegisterRequest, type UserRole} from '../../types/user/UserTypes.ts';

const Register: React.FC = () => {
    const { register } = useAuth();
    const [formData, setFormData] = useState<RegisterRequest>({
        username: '',
        email: '',
        password: '',
        role: "USER"
    });
    const [error, setError] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        try {
            await register(formData);
        } catch (err) {
            console.log('Registration failed. Username or email may already exist. \n' + err);
            setError('Registration failed. Username or email may already exist.');
        }
    };

    return (
        <div className="register-container">
            <h2>Register</h2>
            {error && <div className="error">{error}</div>}

            <form onSubmit={handleSubmit}>
                <div>
                    <label>Username:</label>
                    <input
                        type="text"
                        value={formData.username}
                        onChange={(e) => setFormData({...formData, username: e.target.value})}
                        required
                    />
                </div>

                <div>
                    <label>Email:</label>
                    <input
                        type="email"
                        value={formData.email}
                        onChange={(e) => setFormData({...formData, email: e.target.value})}
                        required
                    />
                </div>

                <div>
                    <label>Password:</label>
                    <input
                        type="password"
                        value={formData.password}
                        onChange={(e) => setFormData({...formData, password: e.target.value})}
                        required
                    />
                </div>

                <div>
                    <label>Role:</label>
                    <select
                        value={formData.role}
                        onChange={(e) => setFormData({...formData, role: e.target.value as UserRole})}
                    >
                        <option value="USER">User</option>
                        <option value="ADVISOR">Beauftragte</option>
                        <option value="ADMIN">Admin</option>
                    </select>
                </div>

                <button type="submit">Register</button>
            </form>
        </div>
    );
};

export default Register;